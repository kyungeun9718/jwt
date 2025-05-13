package com.aladin.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aladin.user.dto.UserDTO;
import com.aladin.user.entity.Member;

@Mapper
public interface UserMapper {

    String findByMemberNoForUpdate(@Param("memberNo") String memberNo);

    int countByMemberId(String memberId);

	void insert(UserDTO userDto);

	Member findByMemberId(String memberId);

	void update(Member member);

	void deleteByMemberId(String memberId);
}
