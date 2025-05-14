package com.aladin;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TodoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    // 테스트용 고정 데이터
    private final String memberId    = "testuser11";
    private final String memberName  = "이경은";
    private final String memberPw    = "password123";
    private final String todoTitle   = "Test Todo";
    private final String todoContent = "Test Content";
    private final String updateTitle   = "Updated Title";
    private final String updateContent = "Updated Content";

    @BeforeEach
    void setUp() throws Exception {
        // 회원가입
        String signupJson = String.format(
            "{\"memberId\":\"%s\",\"memberName\":\"%s\",\"memberPw\":\"%s\"}",
            memberId, memberName, memberPw
        );
        mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson))
               .andExpect(status().isCreated())
               .andDo(print());
        System.out.println("✅ 회원가입 완료");

        // 로그인 & 토큰 추출
        String loginJson = String.format(
            "{\"memberId\":\"%s\",\"memberPw\":\"%s\"}",
            memberId, memberPw
        );
        MvcResult loginResult = mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
               .andExpect(status().isOk())
               .andDo(print())
               .andReturn();

        String loginBody = loginResult.getResponse().getContentAsString();
        token = JsonPath.read(loginBody, "$.message");
        System.out.println("JWT 토큰 획득: " + token);
    }

    @Test
    void todoCrudFlow_withGetForTodoNo() throws Exception {
        String createJson = String.format(
            "{\"todoTitle\":\"%s\",\"todoContent\":\"%s\"}",
            todoTitle, todoContent
        );
        mockMvc.perform(post("/todos")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
               .andExpect(status().isCreated())
               .andDo(print());
        System.out.println("✅  TODO 생성 완료");

        // TODO 목록조회
        MvcResult listResult = mockMvc.perform(get("/todos")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
               .andExpect(status().isOk())
               .andDo(print())
               .andReturn();

        String listBody = listResult.getResponse().getContentAsString();
        System.out.println("✅  조회된 TODO 목록: " + listBody);
        String todoNo = JsonPath.read(listBody, "$[0].todoNo");

        // TODO 수정
        String updateJson = String.format(
            "{\"todoTitle\":\"%s\",\"todoContent\":\"%s\",\"completed\":1}",
            updateTitle, updateContent
        );
        mockMvc.perform(put("/todos/" + todoNo)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
               .andExpect(status().isOk())
               .andDo(print());
        System.out.println("✅  TODO 수정 완료: " + todoNo);

        // TODO 삭제
        mockMvc.perform(delete("/todos/" + todoNo)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
               .andExpect(status().isOk())
               .andDo(print());
        System.out.println("✅  TODO 삭제 완료: " + todoNo);

        System.out.println("✅ 전체 CRUD 흐름 완료");
    }
}
