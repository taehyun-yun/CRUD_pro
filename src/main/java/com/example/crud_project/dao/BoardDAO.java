package com.example.crud_project.dao;

import com.example.crud_project.dto.Board;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("dao")
public class BoardDAO {

    @Autowired
    SqlSessionTemplate session;

    public List<Board> selectListAll() {
        return session.selectList("BoardMapper.selectListAll");
    }

    public int insert(Board board) {
        return session.insert("BoardMapper.insert", board);
    }

    public int delete(int id) {
        return session.delete("BoardMapper.delete", id);
    }

    public int update(Board board) {
        return session.update("BoardMapper.update", board);
    }

    public List<Board> selectList(int id) {
        return session.selectList("BoardMapper.selectid", id);
    }

}
