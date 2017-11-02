package com.cetcme.springBootDemo.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by qiuhong on 31/10/2017.
 */
public interface UsersRepository extends MongoRepository<Users, Long> {

    Users findByUsername(String username);

}
