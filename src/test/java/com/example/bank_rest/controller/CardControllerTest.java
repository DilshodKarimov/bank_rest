package com.example.bank_rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.internal.filter.ValueNodes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void main_test() throws Exception{

        // регистрируем пользователя и сразу авторизуем его
        String requestBody = """
            {
                "username": "user3",
                "password": "1",
                "confirmPassword": "1"
            }
        """;

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String jwt = result.getResponse().getContentAsString();
        String token = new ObjectMapper().readTree(jwt).get("token").asText();


        mockMvc.perform(get("/card/")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());


        // регистрируем админа и сразу авторизуем и создадим карту для пользователя3
        String requestBodyForAdmin = """
            {
                "username": "admin1",
                "password": "1",
                "confirmPassword": "1",
                "code": "5432"
            }
        """;

        mockMvc.perform(post("/registration-admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyForAdmin))
                .andExpect(status().isOk());

        MvcResult resultAdmin = mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyForAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String jwtAdmin = resultAdmin.getResponse().getContentAsString();
        String tokenAdmin = new ObjectMapper().readTree(jwtAdmin).get("token").asText();

        String cardRequest = """
                {
                    "expired": "2026-11-01",
                    "username": "user3",
                    "balance": "10001"
                }
                """;


        mockMvc.perform(post("/admin/card/create")
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(cardRequest))
                .andExpect(status().isOk());



        MvcResult getCard = mockMvc.perform(get("/card/")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode node = new ObjectMapper().readTree(getCard.getResponse().getContentAsString());
        JsonNode card = node.get(0);
        String getId = card.get("id").asText();


        // создадим нового пользователя и создадим ему карту
        String requestBody1 = """
            {
                "username": "user4",
                "password": "1",
                "confirmPassword": "1"
            }
        """;

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody1))
                .andExpect(status().isOk());

        String cardRequestToUser4 = """
                {
                    "expired": "2026-11-01",
                    "username": "user4",
                    "balance": "10001"
                }
                """;

        MvcResult result1 = mockMvc.perform(post("/admin/card/create")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cardRequest))
                .andExpect(status().isOk())
                .andReturn();

        String getResponseCardUser4 = result1.getResponse().getContentAsString();
        String getCardId = new ObjectMapper().readTree(getResponseCardUser4).get("id").asText();

        // делаем транзакцию с id на id пользователей user3 и user4
        String transaction = String.format("""
                {
                    "fromId": "%s",
                    "toId": "%s",
                    "toCardNumber": "-1",
                    "description": "",
                    "amount": "999"
                }
                """, getId, getCardId);

        mockMvc.perform(post("/card/transactions")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(transaction))
                .andExpect(status().isOk());

    }


}