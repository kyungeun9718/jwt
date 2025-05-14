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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TodoSearchIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    // 테스트용 계정 정보
    private final String memberId    = "searchuser19";
    private final String memberName  = "이경은";
    private final String memberPw    = "searchPass123";

    // 검색 테스트용 TODO 정보
    private final String todoTitle1   = "todo Title";
    private final String todoContent1 = "content";
    private final String todoTitle2   = "todo";
    private final String todoContent2 = "content";

    // 검색 키워드 변수
    private final String searchKeyword = "Title";

    @BeforeEach
    void setUp() throws Exception {
        // 1) 회원가입
        String signupJson = String.format(
            "{\"memberId\":\"%s\",\"memberName\":\"%s\",\"memberPw\":\"%s\"}",
            memberId, memberName, memberPw
        );
        System.out.println("🔐 회원가입 요청 JSON: " + signupJson);
        mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson))
               .andExpect(status().isCreated())
               .andDo(print());
        System.out.println("✅ 회원가입 완료");

        // 2) 로그인 및 토큰 획득
        String loginJson = String.format(
            "{\"memberId\":\"%s\",\"memberPw\":\"%s\"}",
            memberId, memberPw
        );
        System.out.println("로그인 요청 JSON: " + loginJson);
        MvcResult loginResult = mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
               .andExpect(status().isOk())
               .andDo(print())
               .andReturn();

        String loginBody = loginResult.getResponse().getContentAsString();
        token = JsonPath.read(loginBody, "$.message");
        System.out.println("JWT 토큰: " + token);
    }

    @Test
    void shouldReturnSearchResultsForKeyword() throws Exception {
        // 첫번째 todo
        String createJson1 = String.format(
            "{\"todoTitle\":\"%s\",\"todoContent\":\"%s\"}",
            todoTitle1, todoContent1
        );
        System.out.println("TODO 생성1: " + createJson1);
        mockMvc.perform(post("/todos")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson1))
               .andExpect(status().isCreated())
               .andDo(print());
        System.out.println("TODO 생성1 완료");

        // 두번째 todo
        String createJson2 = String.format(
            "{\"todoTitle\":\"%s\",\"todoContent\":\"%s\"}",
            todoTitle2, todoContent2
        );
        System.out.println("TODO 생성2: " + createJson2);
        mockMvc.perform(post("/todos")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson2))
               .andExpect(status().isCreated())
               .andDo(print());
        System.out.println("🟢 TODO 생성2 완료");

        // 조회
        System.out.println("조회: keyword=" + searchKeyword);
        MvcResult result = mockMvc.perform(get("/todos/search")
                .param("todoTitle", searchKeyword)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
               .andExpect(status().isOk())
               .andDo(print())
               .andReturn();

        // 검증
        String body = result.getResponse().getContentAsString();
        System.out.println("조회 응답: " + body);
        String firstTitle = JsonPath.read(body, "$[0].todoTitle");
        assertEquals(todoTitle1, firstTitle, "검색 결과의 첫 번째 제목이 일치해야 합니다.");
        System.out.println("조회 결과 검증 완료");
    }
}
