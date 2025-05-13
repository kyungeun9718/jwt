package com.aladin.user.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String memberId;
    private String memberPw;
}
