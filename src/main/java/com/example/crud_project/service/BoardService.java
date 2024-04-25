package com.example.crud_project.service;

import com.example.crud_project.dao.BoardDAO;
import com.example.crud_project.dto.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("service")
public class BoardService {

    @Autowired
    BoardDAO dao;
    public List<Board> selectListAll() {
        return dao.selectListAll();
    }

    public int insert(Board board) {
        int insertresult= dao.insert(board);
        return insertresult;
    }

    public int delete(int id) {
        int deleteresult= dao.delete(id);
        return deleteresult;
    }

    public int update(Board board) {
        int updateresult= dao.update(board);
        System.out.println(board);
        return updateresult;
    }

    public List<Board> selectid(int id) {
        return dao.selectList(id);
    }

}
