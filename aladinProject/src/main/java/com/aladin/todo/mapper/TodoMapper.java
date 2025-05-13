package com.aladin.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aladin.todo.dto.TodoDTO;
import com.aladin.todo.entity.Todo;

@Mapper
public interface TodoMapper {
	void insert(Todo todo);

	List<Todo> findByMemberNo(String memberNo);

	Todo findByTodoNoAndMemberNo(String todoNo, String memberNo);

	void updateTodo(Todo todo);

	void deleteTodo(String todoNo, String memberNo);

	List<Todo> searchByConditions(String memberNo, TodoDTO dto);
}
