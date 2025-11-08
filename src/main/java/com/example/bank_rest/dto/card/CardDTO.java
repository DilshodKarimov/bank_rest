package com.example.bank_rest.dto.card;

import com.example.bank_rest.entity.CardStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CardDTO {
    private Long id;
    private String cardNumber = "**** **** ****";
    private LocalDate expiryDate;
    private CardStatus status;
    private Long balance;


    public void setCardNumber(String cardNumber){
        String q = cardNumber.substring(cardNumber.length()-4);
        this.cardNumber += " " + q;
    }
}
