package com.demo.tutorial;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface TutorialRepository extends ReactiveCrudRepository<Tutorial, Long> {

    @Query("SELECT * FROM tutorials WHERE title = :title")
    Flux<Tutorial> findByTitle(String title);

}