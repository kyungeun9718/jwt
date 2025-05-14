package com.aladin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class InvalidIdAccessTest {

    @Autowired
    private MockMvc mockMvc;

    // 테스트용 잘못된 자격증명 변수
    private final String memberId  = "invaliduser";
    private final String memberPw  = "wrongpassword";

    @Test
    void shouldReturn404WhenLoggingInWithInvalidCredentials() throws Exception {
        System.out.println("test: login with invalid id && pw");

        String loginJson = String.format(
            "{\"memberId\":\"%s\",\"memberPw\":\"%s\"}",
            memberId, memberPw
        );

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
               .andDo(print())
               .andExpect(status().isNotFound());

        System.out.println("404 Not Found response confirmed for invalid login");
    }
}
