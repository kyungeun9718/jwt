package com.aladin.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.common.dto.ApiResponse;
import com.aladin.user.dto.LoginRequestDTO;
import com.aladin.user.dto.UserDTO;
import com.aladin.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	//회원가입
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> signup(@RequestBody UserDTO userDto) {
		return userService.signup(userDto);

	}
	
	//로그인
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDTO loginDto) {
	    return userService.login(loginDto);
	}
	
	//내정보확인
	@GetMapping("/me")
	public ResponseEntity<ApiResponse> getMyInfo(Authentication authentication) {
	    String memberId = authentication.getName();
	    return ResponseEntity.ok(new ApiResponse(200, memberId + " 님"));
	}

	//내정보수정
	@PutMapping("/me")
	public ResponseEntity<ApiResponse> updateMyInfo(
	        @RequestBody UserDTO userDto,
	        Authentication authentication
	) {
	    String memberId = authentication.getName();
	    return userService.updateMyInfo(memberId, userDto);
	}

	//회원탈퇴
	@DeleteMapping("/me")
	public ResponseEntity<ApiResponse> deleteMyAccount(Authentication authentication) {
	    String memberId = authentication.getName();
	    return userService.deleteMyAccount(memberId);
	}


}
