package com.aladin;

import com.jayway.jsonpath.JsonPath;
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
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void userTest() throws Exception {

        String memberId   = "testuser13";
        String memberName = "이경은";
        String memberPw   = "password123";
  
        String updateName    = "테스트유저_수정";
        String signupJson = String.format(
            "{\"memberId\":\"%s\",\"memberName\":\"%s\",\"memberPw\":\"%s\"}",
            memberId, memberName, memberPw
        );
        //회원가입
        System.out.println("회원가입 요청 JSON: " + signupJson);
        mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson))
               .andExpect(status().isCreated())
               .andDo(print());
        System.out.println("✅ 회원가입 완료");

        // 로그인
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
        String token = JsonPath.read(loginResult.getResponse().getContentAsString(), "$.message");
        System.out.println("JWT 토큰 : " + token);

        // 조회
        System.out.println("내 정보 조회 시작");
        MvcResult meResult = mockMvc.perform(get("/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
               .andExpect(status().isOk())
               .andDo(print())
               .andReturn();
        System.out.println("내 정보: " + meResult.getResponse().getContentAsString());

        //수정
        String updateJson = String.format(
        	    "{\"memberName\":\"%s\", \"memberPw\":\"%s\"}",
        	    updateName, memberPw
        );
        System.out.println("내 정보 수정 요청 JSON: " + updateJson);
        MvcResult updateResult = mockMvc.perform(put("/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
               .andExpect(status().isOk())
               .andDo(print())
               .andReturn();
        System.out.println("내 정보 수정: " + updateResult.getResponse().getContentAsString());

        // 수정 후 조회
        System.out.println("수정 후 내 정보 조회 시작");
        MvcResult meResult2 = mockMvc.perform(get("/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
               .andExpect(status().isOk())
               .andDo(print())
               .andReturn();
        System.out.println("수정 후 내 정보: " + meResult2.getResponse().getContentAsString());

        // 탈퇴
        System.out.println("탈퇴 요청 시작");
        MvcResult deleteResult = mockMvc.perform(delete("/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
               .andExpect(status().isOk())
               .andDo(print())
               .andReturn();
        System.out.println("탈퇴: " + deleteResult.getResponse().getContentAsString());
    }
}
