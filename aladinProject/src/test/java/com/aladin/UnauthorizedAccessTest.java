package com.aladin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UnauthorizedAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void accessingWithoutJwt() throws Exception {
        System.out.println("Starting test: without JWT");
        mockMvc.perform(get("/todos"))
               .andDo(print())
               .andExpect(status().isUnauthorized());
        System.out.println("401 Unauthorized response confirmed");
    }
}
