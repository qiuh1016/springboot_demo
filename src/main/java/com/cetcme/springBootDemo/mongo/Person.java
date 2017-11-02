package com.cetcme.springBootDemo.mongo;

/**
 * Created by qiuhong on 31/10/2017.
 */
public class Person {

    private String id;
    private String name;
    private Integer age;

    public Person(String name, Integer age) {
        this.id = null;
        this.name = name;
        this.age = age;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getAge() {
        return this.age;
    }

}