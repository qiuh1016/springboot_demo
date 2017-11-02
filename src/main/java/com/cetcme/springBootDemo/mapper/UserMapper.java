package com.cetcme.springBootDemo.mapper;

import com.cetcme.springBootDemo.entity.UserEntity;

import java.util.List;

/**
 * Created by qiuhong on 01/11/2017.
 */
public interface UserMapper {

    List<UserEntity> getAll();

    UserEntity getOne(Long id);

    void insert(UserEntity user);

    void update(UserEntity user);

    void delete(Long id);

}