package com.cetcme.springBootDemo.mongo;

import java.util.Date;

/**
 * Created by qiuhong on 31/10/2017.
 */
public class Users {

    private String id;
    private String username;
    private String password;
    private String rank;
    private String major;
    private String school;
    private String education;
    private String hire_date;
    private String birth_place;
    private String department;
    private String phone_1;
    private String phone_2;
    private Date update_at;
    private Date create_at;
    private Boolean left_company;
    private Boolean need_submit_report;
    private Integer permission;
    private String email;
    private Boolean active;

    public Users(String username, String password) {
        this.id = null;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String setPassword(String pwd) {
        return this.password = pwd;
    }

    public Date setUpdateAt() {
        return this.update_at = new Date();
    }
}
