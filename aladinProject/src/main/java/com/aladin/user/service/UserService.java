package com.aladin.user.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.aladin.common.dto.ApiResponse;
import com.aladin.common.config.PasswordEncoderConfig;
import com.aladin.user.validation.UserValidator;
import com.aladin.user.dto.LoginRequestDTO;
import com.aladin.user.dto.LoginResponseDTO;
import com.aladin.user.dto.UserDTO;
import com.aladin.user.mapper.UserMapper;
import com.aladin.user.entity.Member;
import com.aladin.common.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class UserService {
	private final UserMapper userMapper;
	private final UserValidator userValidator;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;


	//회원가입
	@Transactional
	public ResponseEntity<ApiResponse> signup(UserDTO userDto) {
	    try {
	        validateUserSignupRequest(userDto); //회원가입 validation 확인

	        prepareUserForInsert(userDto); //비밀번호 암호화 + 회원번호 생성

	        userMapper.insert(userDto);

	        return ResponseEntity
	                .status(HttpStatus.CREATED)
	                .body(new ApiResponse(HttpStatus.CREATED.value(),"회원가입 성공"));

	    } catch (IllegalArgumentException e) {
	        return ResponseEntity
	                .badRequest()
	                .body(new ApiResponse(HttpStatus.BAD_REQUEST.value(),"회원가입 실패: " + e.getMessage()));

	    } catch (Exception e) {
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"서버 오류가 발생했습니다."));
	    }
		
	}
	
	//로그인
	@Transactional
	public ResponseEntity<ApiResponse> login(LoginRequestDTO loginDto) {
	    try {
	        Member member = findMemberOrThrow(loginDto.getMemberId());
	        validatePassword(loginDto.getMemberPw(), member.getMemberPw());
	        String token = generateAccessToken(member.getMemberId());
	        return ResponseEntity.ok(new ApiResponse(200, token));
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "로그인 실패: " + e.getMessage()));
	    }
	}
	
	//내정보 수정
	@Transactional
	public ResponseEntity<ApiResponse> updateMyInfo(String memberId, UserDTO dto) {
	    try {
	    	userValidator.validateUpdate(dto);
	    	
	        Member member = findMemberOrThrow(memberId);
	        applyUpdateToMember(member, dto);
	        userMapper.update(member);
	        return ResponseEntity.ok(new ApiResponse(200, "회원 정보가 수정되었습니다."));
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse(404, e.getMessage()));
	    }
	}
	
	//회원 탈퇴
	@Transactional
	public ResponseEntity<ApiResponse> deleteMyAccount(String memberId) {
		Member member = findMemberOrThrow(memberId);
		 
	    userMapper.deleteByMemberId(memberId);

	    return ResponseEntity.ok(new ApiResponse(200, "회원 탈퇴가 완료되었습니다."));
	}



	
	//회원가입 validation 확인
	private void validateUserSignupRequest(UserDTO userDto) {
	    userValidator.validateSignup(userDto);

	    int count = userMapper.countByMemberId(userDto.getMemberId());


	    if (count > 0) {
	        throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
	    }
	}
	
	//비밀번호 암호화 + 회원번호 생성
	private void prepareUserForInsert(UserDTO userDto) {
	    String memberNo = generateUniqueMemberNo();
	    String encryptedPw = passwordEncoder.encode(userDto.getMemberPw());

	    userDto.setMemberNo(memberNo);
	    userDto.setMemberPw(encryptedPw);
	}


	
	//unique한 회원번호 찾기
    private String generateUniqueMemberNo() {
        int maxRetries = 5;
        for (int i = 0; i < maxRetries; i++) {
            String memberNo = generateMemberNo();
            if (userMapper.findByMemberNoForUpdate(memberNo) == null) {
                return memberNo;
            }
        }
        throw new IllegalStateException("회원번호 생성 실패: 중복된 번호가 반복적으로 발생했습니다.");
    }

    //회원번호 확인
    private String generateMemberNo() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%05d", (int)(Math.random() * 100000));
        return now + random;
    }
    
    //회원 확인
    public Member findMemberOrThrow(String memberId) {
        Member member = userMapper.findByMemberId(memberId);
        if (member == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 회원입니다.");
        }
        return member;
    }

    private void validatePassword(String rawPw, String encodedPw) {
        if (!passwordEncoder.matches(rawPw, encodedPw)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private String generateAccessToken(String memberId) {
        return jwtTokenProvider.createToken(memberId);
    }

    //내정보 수정 > 이름, 비밀번호
    private void applyUpdateToMember(Member member, UserDTO dto) {
        if (dto.getMemberName() != null) {
            member.setMemberName(dto.getMemberName());
        }

        if (dto.getMemberPw() != null && !dto.getMemberPw().isBlank()) {
            String encrypted = passwordEncoder.encode(dto.getMemberPw());
            member.setMemberPw(encrypted);
        }
    }


}
