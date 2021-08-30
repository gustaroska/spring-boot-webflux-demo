package com.demo.student;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.CustomObjectMapper;
import com.demo.ResponseWrapper;
import com.demo.ResponseWrapperList;
import com.demo.kafka.KafkaConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {
	
	private final static String SUBMIT_ACTION = "submit";
	
	private final static String EDIT_ACTION = "edit";
	
	private final static String UNDO_DELETE_ACTION = "undo";
	
	@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private StudentRedisService studentRedisService;

	
	@GetMapping("/redis")
    public Mono<ResponseEntity<ResponseWrapperList>> getAllFromRedis() {
		
		Mono<List<Student>> result = studentRedisService.findAll().collectList();
        
        return result
        		.map(s -> ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapperList(s)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseWrapperList(HttpStatus.NO_CONTENT.getReasonPhrase(),HttpStatus.NO_CONTENT.value())));
    }
	
	@GetMapping("/redis/{id}")
    public Mono<ResponseEntity<ResponseWrapper>> getByIdFromRedis(
    		@PathVariable String id) {
		
		Mono<Student> result = studentRedisService.findById(id);
		
        return result
        		.map(s -> ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(s)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND.value())));
    }
	
	@GetMapping("/{id}")
    public Mono<ResponseEntity<ResponseWrapper>> getById(
    		@PathVariable String id) {
		
		Mono<Student> result = studentRedisService.findById(id) // 1. find in redis, if not found then
				.switchIfEmpty(studentRepository.findById(id).flatMap(it -> { // 2. find in database, then
						return studentRedisService.save(it); // 3. put to redis
				}));
		
        return result
        		.map(s -> ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(s)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper(HttpStatus.NOT_FOUND.getReasonPhrase(),HttpStatus.NOT_FOUND.value())));
    }
	
	@GetMapping
    public  Mono<ResponseEntity<ResponseWrapperList>> getListByParam(
    		@RequestParam(name = "name", required = false) String name) {
		Mono<List<Student>> result = studentRepository.findByName("%"+name+"%").collectList();
        
        return result
        		.map(s -> ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapperList(s)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseWrapperList(HttpStatus.NO_CONTENT.getReasonPhrase(),HttpStatus.NO_CONTENT.value())));
    }
	
	
	@PostMapping
    public Mono<ResponseEntity<ResponseWrapper>> create(
    		@RequestParam(name = "action", required = false) String action, // available action: SUBMIT
    		@RequestBody Student student) {
		
		student = validate(student, new Student(UUID.randomUUID().toString()), action);
		
		Mono<Student> result = studentRedisService.save(student).flatMap(v -> (
				
				v.getStatus().equals(
						StudentRedisService.SUBMITTED_STATUS)? // insert into database when status = submitted
								studentRepository.save(v):Mono.just(v)
		
		))
				.doOnSuccess(v -> {
					try {
						this.kafkaTemplate.send(KafkaConfiguration.TOPIC, new CustomObjectMapper().writeValueAsString(v));
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}); // -> Kafka
        
        return result
        		.map(s -> ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(s)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),HttpStatus.INTERNAL_SERVER_ERROR.value())));
    }

	
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ResponseWrapper>> update(
    		@PathVariable String id, 
    		@RequestParam(name = "action", required = false) String action, // available action: SUBMIT | EDIT
    		@RequestBody(required = false) Student student) {
    	
    	
    	Mono<Student> result = studentRedisService.findById(id).switchIfEmpty(studentRepository.findById(id)).map(v -> (validate(student, v, action))).flatMap(v -> (studentRedisService.update(id, v).flatMap(x -> (
    		v.getStatus().equals(
					StudentRedisService.SUBMITTED_STATUS)? // insert into database when status = submitted
							studentRepository.save(v):Mono.just(x)
    	))));

    	
        return result
        		.map(s -> ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(s)))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),HttpStatus.INTERNAL_SERVER_ERROR.value())));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ResponseWrapper>> delete(
    		@PathVariable String id,
    		@RequestParam(name = "action", required = false) String action // available action: UNDO_DELETE
    		) {
    	
    	if(action != null && action.equals(UNDO_DELETE_ACTION)) {
    		
    		Mono<Student> result = studentRedisService.findById(id).flatMap(v -> (studentRepository.save(v)));
    		
    		return result
            		.map(s -> ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(s)))
                    .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),HttpStatus.INTERNAL_SERVER_ERROR.value())));
    		
    	}
    	
    	
    	Mono<Void> result = studentRepository.findById(id).flatMap(v -> {
    		
    		v.setDeletedDate(LocalDateTime.now());
    		return studentRedisService.save(v);
    		
    	}).then(studentRepository.deleteById(id));
    	
        return result.then(Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper())));
    }
    
    private Student validate(Student _student, Student data, String action) {
    	Student student = data;
    	

    	if(_student != null) {

	    	student.setName(_student.getName());
	    	student.setMale(_student.getMale());
	    	student.setGrade(_student.getGrade()); // check if user granted to change this value
	
	    	if(data.getCreatedDate() != null) {
	    		student.setLastModifiedDate(LocalDateTime.now());	
	    	}
	    	
    	}
    	
    	if(action == null) {
    		if(student.getStatus() == null) student.setStatus(StudentRedisService.DRAFTED_STATUS);
		}else {
			
			if(action.equals(SUBMIT_ACTION)) { // make sure user login is authorized for this action
				student.setStatus(StudentRedisService.SUBMITTED_STATUS); 
			}	
			
			if(action.equals(EDIT_ACTION)) { // make sure user login is authorized for this action
				student.setStatus(StudentRedisService.DRAFTED_STATUS); 
			}	
			

		}
    	return student;
    }
}
