package com.demo.tutorial;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TutorialService {
	
	@Autowired
	TutorialRepository tutorialRepository;
	
	public Flux<Tutorial> findTutorialsByTitle(String title) {
        return (title != null) ? tutorialRepository.findByTitle(title) : tutorialRepository.findAll();
    }
	
	public Mono<Tutorial> findTutorialById(long id) {
		
        return tutorialRepository.findById(id);
    }
	
	
	public Mono<Tutorial> addNewTutorial(Tutorial tutorial) {
		tutorial.setCratedDate(LocalDateTime.now());
		
        return tutorialRepository.save(tutorial);
    }

    public Mono<Tutorial> updateTutorial(long id, Tutorial tutorial) {
        return tutorialRepository.findById(id)
                .flatMap(s -> {
                    tutorial.setId(s.getId());
                    return tutorialRepository.save(tutorial);
                });

    }

    public Mono<Void> deleteTutorial(Tutorial tutorial) {
        return tutorialRepository.delete(tutorial);
    }

}
