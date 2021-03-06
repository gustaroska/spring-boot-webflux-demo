package com.demo.tutorial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/redis")
public class TutorialRedisController {
	
	@Autowired
	TutorialRedisService tutorialRedisService;
	
	@GetMapping("/tutorials")
    public Flux<Tutorial> listTutorials() {
        return tutorialRedisService.findAll();
    }

	
	@PostMapping("/tutorials")
    public Mono<Boolean> addNewTutorial(@RequestBody Tutorial tutorial) {
        return tutorialRedisService.addNew(tutorial);
    }

}
