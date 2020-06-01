package org.example.repository;

import org.example.entity.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Integer> {

    //根据用户名 获取用户
    User findByUsername(String username);
}