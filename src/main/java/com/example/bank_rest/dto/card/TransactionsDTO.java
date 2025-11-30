package com.example.bank_rest.dto.card;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsDTO {
    private Long fromId;
    private Long toId;
    private String description;
    private Long amount;
}
