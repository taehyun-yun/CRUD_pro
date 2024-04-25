package com.example.crud_project.dao;

import com.example.crud_project.dto.User;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class UserDAO {

    @Autowired
    SqlSessionTemplate session;

    private final String url = "jdbc:h2:mem:testdb";
    private final String username = "sa";
    private final String password = "";

    // findBy 규칙 -> username 문멉
    public User findByUsername(String username){
        System.out.println(" 유저 DAO =========");
        return session.selectOne("UserMapper.findByUsername", username);
    }

    public void save(User user) {
        session.insert("UserMapper.save", user);
    }


    // 기타 필요한 메서드들을 추가할 수 있습니다.
}

