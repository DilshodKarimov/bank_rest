package com.example.bank_rest.dto.card;

import lombok.Data;

import java.security.Timestamp;
import java.time.LocalDate;

@Data
public class TransactionsResponseDTO {
    private String status = "SUCCESS";
    private String message = "Перевод выполнен успешно";
}
