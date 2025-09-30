package com.example.bank_rest.dto.card;

import com.example.bank_rest.entity.CardStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CardStatusDTO {
    private CardStatus status;
    private LocalDate expiryDate;
}
