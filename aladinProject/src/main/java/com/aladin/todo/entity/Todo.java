package com.aladin.todo.entity;

import lombok.Data;

@Data
public class Todo {
    private String todoNo;
    private String memberNo;
    private String todoTitle;
    private String todoContent;
    private Integer completed = 0; //0이면 미완료 1이면 완
    private String instDtm;
    private String updateDtm;
}
