package com.cetcme.springBootDemo.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by qiuhong on 31/10/2017.
 */
public interface PersonRepository extends MongoRepository<Person, Long> {

    Person findByName(String name);

    Person[] findAllByName(String name);

}
