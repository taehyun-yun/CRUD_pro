package com.example.crud_project.dto;

import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias("Board")
public class Board {
    private int id;
    private String title;
    private String content;
    private Date creationtime;
    private Date updatetime;

    public Board(){
        super();
    }

    public Board(int id, String title, String content, Date creationtime, Date updatetime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationtime = creationtime;
        this.updatetime = updatetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(Date creationtime) {
        this.creationtime = creationtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", creationtime='" + creationtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}
