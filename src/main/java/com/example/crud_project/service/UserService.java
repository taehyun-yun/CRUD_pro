package com.example.crud_project.service;

import com.example.crud_project.dao.UserDAO;
import com.example.crud_project.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService {

    @Autowired
    UserDAO userdao;

    public UserService(UserDAO userdao){
        this.userdao= userdao;
    }

    public User findByUsername(String username){
        User user= userdao.findByUsername(username);
        if (user == null){
            return null;
        }
        return user;
    }// end user



}// end class
