package com.cetcme.springBootDemo.controller;

import com.cetcme.springBootDemo.mongo.Users;
import com.cetcme.springBootDemo.mongo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloAll() {
        return "hello world! This is the main page";
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String save() {
        return "hello world";
    }

    @RequestMapping(value = "/getUsername", method = RequestMethod.GET)
    public String getUsername(String name) {
        Users users = usersRepository.findByUsername(name);
        if (users != null) {
            return "hello world, get mongo pwd: "  + users.getPassword();
        } else {
            return "hello world, get mongo: null";
        }

    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.GET)
    public String updatePassword(String name) {
        Users users = usersRepository.findByUsername(name);
        if (users != null) {
            users.setPassword("881016");
            users.setUpdateAt();
            usersRepository.save(users);
            return "hello world, update mongo: "  + users.getUsername();
        } else {
            return "hello world, get mongo: null";
        }

    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(String name) {
        Users users = usersRepository.findByUsername(name);
        if (users != null) {
            usersRepository.delete(users);
            return "hello world, update mongo: "  + users.getUsername();
        } else {
            return "hello world, get mongo: null";
        }

    }

}