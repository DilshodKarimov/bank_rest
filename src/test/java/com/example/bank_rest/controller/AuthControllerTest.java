package com.example.bank_rest.controller;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @Test // register a new user
    void createUser() throws Exception{
        String requestBody = """
            {
                "username": "user1",
                "password": "1",
                "confirmPassword": "1"
            }
        """;

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test // register a user with different password and password confirm
    void createUser1() throws Exception{
        String requestBody = """
            {
                "username": "user2",
                "password": "2",
                "confirmPassword": "1"
            }
        """;

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }


    @Test // auth a new user with bad password
    void createAuthToken1() throws Exception{
        String requestBody = """
            {
                "username": "user1",
                "password": "2"
            }
        """;

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());

    }

    @Test // auth a new user with bad username and password
    void createAuthToken2() throws Exception{
        String requestBody = """
            {
                "username": "usertestsdfmosefm",
                "password": "1"
            }
        """;

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test // register a new admin
    void testCreateUser() {
    }
}