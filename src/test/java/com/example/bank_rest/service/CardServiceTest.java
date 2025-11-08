package com.example.bank_rest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class CardServiceTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getCards() {
        
    }

    @Test
    void responseBlockCard() {
    }

    @Test
    void getCardById() {
    }

    @Test
    void transactions() {
    }
}