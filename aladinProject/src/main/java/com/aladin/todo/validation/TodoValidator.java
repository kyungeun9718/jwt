package com.aladin.todo.validation;

import org.springframework.stereotype.Component;

import com.aladin.todo.dto.TodoDTO;

@Component
public class TodoValidator {

	public void validateCreateTodo(TodoDTO dto) {
	    if (dto.getTodoTitle() == null || dto.getTodoTitle().isBlank()) {
	    	throw new IllegalArgumentException("제목은 필수입니다.");
	    }
		
	    if (dto.getTodoContent() == null || dto.getTodoContent().isBlank()) {
	    	throw new IllegalArgumentException("내용은 필수입니다.");
	    }
	}

	
}
