package com.aladin.todo.dto;

import lombok.Data;

@Data
public class TodoDTO {

	private String todoTitle;
	private String todoContent;
	private Integer completed = 0; //0이면 미완료 1이면 완
}
