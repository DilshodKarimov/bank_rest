package com.example.bank_rest.dto.card;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TransactionsDTO {
    private Long fromId;
    private String toCardNumber;
    private String description;
    private Long amount;
}
