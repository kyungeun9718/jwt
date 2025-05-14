package com.aladin;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthIntegrationTest {
//회원가입_로그인_JWT_인증_흐름_테스트
    @Autowired
    private MockMvc mockMvc;

    @Test
    void jwtCertification() throws Exception {

        String memberId   = "tesuser1";
        String memberName = "이경은";
        String memberPw   = "password123";


        String signupJson = String.format(
            "{\"memberId\":\"%s\",\"memberName\":\"%s\",\"memberPw\":\"%s\"}",
            memberId, memberName, memberPw
        );
        String loginJson = String.format(
            "{\"memberId\":\"%s\",\"memberPw\":\"%s\"}",
            memberId, memberPw
        );

        // 회원가입
        System.out.println("회원가입 시작");
        mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson))
                .andExpect(status().isCreated());

        // 로그인
        System.out.println("로그인 시작");
        MvcResult loginResult = mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String token = JsonPath.read(loginResult.getResponse().getContentAsString(), "$.message");
        System.out.println("토큰 획득 완료: " + token);

        // TODO 조회
        System.out.println("TODO 조회 시작");
        mockMvc.perform(get("/todos")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());

        System.out.println("전체 흐름 테스트 성공");
    }

}
