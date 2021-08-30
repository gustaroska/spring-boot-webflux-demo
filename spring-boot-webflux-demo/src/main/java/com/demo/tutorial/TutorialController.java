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
@RequestMapping("/api")
public class TutorialController {
	
	@Autowired
	TutorialService tutorialService;
	
	@GetMapping("/tutorials")
    public Flux<Tutorial> listTutorials(@RequestParam(name = "title", required = false) String title) {
        return tutorialService.findTutorialsByTitle(title);
    }

	@GetMapping("/tutorials/{id}")
    public Mono<ResponseEntity<Tutorial>> getTutorial(@PathVariable long id) {
        return tutorialService.findTutorialById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
	
	@PostMapping("/tutorials")
    public Mono<Tutorial> addNewTutorial(@RequestBody Tutorial tutorial) {
        return tutorialService.addNewTutorial(tutorial);
    }

    @PutMapping("/tutorials/{id}")
    public Mono<ResponseEntity<Tutorial>> updateTutorial(@PathVariable long id, @RequestBody Tutorial tutorial) {
        return tutorialService.updateTutorial(id, tutorial)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/tutorials/{id}")
    public Mono<ResponseEntity<Void>> deleteTutorial(@PathVariable long id) {
        return tutorialService.findTutorialById(id)
                .flatMap(s ->
                        tutorialService.deleteTutorial(s)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
