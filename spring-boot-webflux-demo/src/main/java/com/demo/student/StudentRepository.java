package com.demo.student;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface StudentRepository  extends ReactiveCrudRepository<Student, String> {

	@Query("SELECT * FROM students WHERE name like :name")
    Flux<Student> findByName(String name);
}
