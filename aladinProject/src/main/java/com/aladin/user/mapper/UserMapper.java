package com.aladin.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aladin.user.dto.UserDTO;
import com.aladin.user.entity.Member;

@Mapper
public interface UserMapper {

	//회원번호 중복 확인
    String findByMemberNoForUpdate(@Param("memberNo") String memberNo);

    int countByMemberId(String memberId);


	void insert(UserDTO userDto);
}
