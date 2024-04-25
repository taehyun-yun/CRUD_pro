package com.example.crud_project.controller;

import com.example.crud_project.dto.Board;
import com.example.crud_project.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class BoardController {
    @Autowired
    BoardService service;

    @RequestMapping("/")
    public String board(Model model){
        List<Board> list= service.selectListAll();
        model.addAttribute("list", list);
        return "board";
    }

    @GetMapping("/add")
    public String addForm() {
        return "add";
    }

    @PostMapping("/add")
    public String add(Board board) {
        int addresult= service.insert(board);
        System.out.println(addresult);
        return "redirect:/";
    }

    @RequestMapping("/delete")
    public String delete(int id, Model model) {
        int deleteresult= service.delete(id);
        System.out.println("삭제완료 "+ deleteresult);
        List<Board> list= service.selectListAll();
        model.addAttribute("list", list);
        return "board";
    }

    @GetMapping("/update")
    public String updateForm(Model model, int id) {
        List<Board> list= service.selectid(id);
        model.addAttribute("list", list);
        return "update";
    }

    @PostMapping("/update")
    public String update(Model model, Board board) {
        int updateresult= service.update(board);
        System.out.println("수정: "+ updateresult);

        List<Board> list= service.selectListAll();
        model.addAttribute("list", list);
        return "redirect:/";
    }
    
}// end class
