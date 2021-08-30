package com.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentRedisService {

	private final static String CACHE = "students";
	

	public final static String DRAFTED_STATUS = "drafted";
	
	public final static String SUBMITTED_STATUS = "submitted";
	
	public final static String DELETED_STATUS = "deleted";
	
	@Autowired
	private ReactiveRedisOperations<String, Student> redisOps;
	
	public Flux<Student> findAll() {
		return redisOps.keys(CACHE+"*")
		        .flatMap(redisOps.opsForValue()::get);
    }
	
	public Mono<Student> findById(String id) {
		Mono<Student> result = redisOps.opsForValue().get(CACHE+"::"+id);
		
		return result;
    }
	
	public Mono<Student> save(Student student) {
		return Mono.fromSupplier(
		        () -> {
		        	redisOps.opsForValue()
		        	.set(CACHE+"::"+student.getId(), student)
		              .subscribe();
		          return student;
		        });
	  }

	  public Mono<Student> update(String id, Student student) {
		  return Mono.fromSupplier(
			        () -> {
			        	redisOps.opsForValue()
			        	.set(CACHE+"::"+id, student)
			              .subscribe();
			          return student;
			        });
	  }

	  public Mono<Void> delete(String id) {
	    return Mono.fromSupplier(
	        () -> {
	        	redisOps.opsForValue()
	              .delete(CACHE+"::"+id)
	              .subscribe();
	          return null;
	        });
	  }
}
