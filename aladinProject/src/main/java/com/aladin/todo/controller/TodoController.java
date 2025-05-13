package com.aladin.todo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.todo.service.TodoService;

import lombok.RequiredArgsConstructor;
import com.aladin.common.dto.ApiResponse;
import com.aladin.todo.dto.TodoDTO;
import com.aladin.todo.entity.Todo;

@RestController
@RequiredArgsConstructor
public class TodoController {

	private final TodoService todoService;
	
	//todo 등록
	@PostMapping("/todos")
	public ResponseEntity<ApiResponse> createTodo( @RequestBody TodoDTO dto, Authentication authentication
	) {
	    String memberId = authentication.getName();
	    return todoService.createTodo(memberId, dto);
	}
	
	//todo 조회
	@GetMapping("/todos")
	public ResponseEntity<List<Todo>> getTodoList(Authentication authentication) {
	    String memberId = authentication.getName(); // JWT로부터 memberId 추출
	    return todoService.getTodosByMember(memberId);
	}

	//todo 단건 조회
	@GetMapping("/todos/{todoNo}")
	public ResponseEntity<ApiResponse> getTodoById(@PathVariable String todoNo, Authentication authentication) {
	    String memberId = authentication.getName();
	    return todoService.getTodoById(todoNo, memberId);
	}


}
