package com.aladin.todo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aladin.common.dto.ApiResponse;
import com.aladin.todo.dto.TodoDTO;
import com.aladin.todo.entity.Todo;
import com.aladin.todo.mapper.TodoMapper;
import com.aladin.todo.validation.TodoValidator;
import com.aladin.user.entity.Member;
import com.aladin.user.mapper.UserMapper;
import com.aladin.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {

	private final UserService userService;
	private final TodoMapper todoMapper;
	private final UserMapper userMapper;
	private final TodoValidator todoValidator;
	
	//등록
	@Transactional
	public ResponseEntity<ApiResponse> createTodo(String memberId, TodoDTO dto) {
		try {
			todoValidator.validateCreateTodo(dto);
			validateUserExistence(memberId);
			
			Member member = userService.findMemberOrThrow(memberId);
			
			Todo todo = createTodoEntity(dto, member.getMemberNo());
			
			todoMapper.insert(todo);
	
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body(new ApiResponse(201, "TODO가 성공적으로 등록되었습니다."));

	    } catch (IllegalArgumentException e) {
	        return ResponseEntity
	                .badRequest()
	                .body(new ApiResponse(400, "TODO 등록 실패: " + e.getMessage()));
	
	    } catch (Exception e) {
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse(500, "서버 오류가 발생했습니다."));
	    }
	}
	
	//조회
	public ResponseEntity<List<Todo>> getTodosByMember(String memberId) {

		try {
			Member member = userService.findMemberOrThrow(memberId);
			String memberNo = member.getMemberNo();
			
			List<Todo> todoList = todoMapper.findByMemberNo(memberNo);
	
	        return ResponseEntity.ok(todoList);
			
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
	   
	    }
	}
	
	//단건 조회
	@Transactional
	public ResponseEntity<ApiResponse> getTodoById(String todoNo, String memberId) {
	    try {
	        Member member = userService.findMemberOrThrow(memberId);
	        String memberNo = member.getMemberNo();

	        Todo todo = todoMapper.findByTodoNoAndMemberNo(todoNo, memberNo);
	        if (todo == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ApiResponse(404, "해당 TODO를 찾을 수 없습니다."));
	        }
	        return ResponseEntity.ok(new ApiResponse(200, todo.toString()));

	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse(404, e.getMessage()));
	    }
	}

	//수정
	@Transactional
	public ResponseEntity<ApiResponse> updateTodo(String todoNo, String memberId, TodoDTO dto) {
	    try {
	        Member member = userService.findMemberOrThrow(memberId);
	        String memberNo = member.getMemberNo();

	        Todo todo = findTodoOrThrow(todoNo, memberNo);

	        applyTodoUpdates(todo, dto);

	        todo.setUpdateDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	        todoValidator.validateUpdateTodo(dto);
	        todoMapper.updateTodo(todo);

	        return ResponseEntity.ok(new ApiResponse(200, "TODO가 성공적으로 수정되었습니다."));

	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse(400, e.getMessage()));
	    }
	}

	private Todo findTodoOrThrow(String todoNo, String memberNo) {
	    Todo todo = todoMapper.findByTodoNoAndMemberNo(todoNo, memberNo);
	    if (todo == null) {
	        throw new IllegalArgumentException("해당 TODO를 찾을 수 없습니다.");
	    }
	    return todo;
	}

	
	

	private void validateUserExistence(String memberId) {
	    int count = userMapper.countByMemberId(memberId);
	    if (count != 1) {
	        throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
	    }
	}
	
	private Todo createTodoEntity(TodoDTO dto, String memberNo) {
	    Todo todo = new Todo();
	    todo.setTodoNo(generateTodoNo());
	    todo.setMemberNo(memberNo);
	    todo.setTodoTitle(dto.getTodoTitle());
	    todo.setTodoContent(dto.getTodoContent());
	    todo.setInstDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	    return todo;
	}
	
	private String generateTodoNo() {
	    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	}

	private Member getMemberNo(String memberId) {
	    return userMapper.findByMemberId(memberId);
	}

	private void applyTodoUpdates(Todo todo, TodoDTO dto) {
        if (dto.getTodoTitle() != null) {
            todo.setTodoTitle(dto.getTodoTitle());
        }

        if (dto.getTodoContent() != null) {
            todo.setTodoContent(dto.getTodoContent());
        }

        if (dto.getCompleted() != null) {
            todo.setCompleted(dto.getCompleted());
        }
	}


	
}
