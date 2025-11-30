package com.example.bank_rest.dto.card;

import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public static CardDTO convertToResponse(Card card){
        CardDTO cardDTO = new CardDTO();

        cardDTO.setId(card.getId());
        cardDTO.setCardNumber(card.getCardNumber());
        cardDTO.setExpiryDate(card.getExpiryDate());
        cardDTO.setStatus(card.getStatus());
        cardDTO.setBalance(card.getBalance());

        return cardDTO;
    }
}
