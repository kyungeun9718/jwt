package com.aladin.user.controller.validation;

import org.springframework.stereotype.Component;

import com.aladin.user.dto.UserDTO;

@Component
public class UserValidator {
	
	//회원가
    public void validateSignup(UserDTO userDto) {
        if (userDto.getMemberId() == null || userDto.getMemberId().isBlank()) {
            throw new IllegalArgumentException("아이디는 필수입니다.");
        }

        if (userDto.getMemberPw() == null || userDto.getMemberPw().isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        if (userDto.getMemberName() == null || userDto.getMemberName().isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }

    }
    
    public void validateUpdate(UserDTO dto) {
        if (dto.getMemberName() == null || dto.getMemberName().isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }

        if (dto.getMemberPw() == null || dto.getMemberPw().isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
    }
}


