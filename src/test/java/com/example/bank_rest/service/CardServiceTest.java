package com.example.bank_rest.service;

import com.example.bank_rest.dto.card.CardDTO;
import com.example.bank_rest.dto.card.TransactionsDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.CardStatus;
import com.example.bank_rest.entity.Transaction;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.TransactionRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CardServiceTest {
    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;



    @InjectMocks
    private CardService cardService;





    @Test
    void getCards() {

        User user = new User();

        List<Card> cards = new ArrayList<>();

        when(cardRepository.findByUser(user)).thenReturn(cards);

        assertTrue(cardService.getCards(1, 10, user).isEmpty());
    }

    @Test
    void responseBlockCard() {
        Card card = new Card();
        card.setId(1L);
        card.setCardNumber("123456789012");
        card.setUser(new User());
        card.setBalance(123L);
        card.setStatus(CardStatus.ACTIVE);
        card.setExpiryDate(LocalDate.of(2026, 12, 1));

        assertTrue(cardService.responseBlockCard(card) instanceof CardDTO);
    }

    @Test
    void getCardById() {
        Card card = new Card();
        card.setId(1L);
        card.setCardNumber("123456789012");
        card.setUser(new User());
        card.setBalance(123L);
        card.setStatus(CardStatus.ACTIVE);
        card.setExpiryDate(LocalDate.of(2026, 12, 1));

        assertTrue(cardService.getCardById(card) instanceof CardDTO);
    }

    @Test
    void transactions() {
        TransactionsDTO transactionsDTO = new TransactionsDTO();
        Card fromCard = new Card();
        fromCard.setId(1L);
        fromCard.setCardNumber("123456789012");
        fromCard.setUser(new User());
        fromCard.setBalance(123L);
        fromCard.setStatus(CardStatus.ACTIVE);
        fromCard.setExpiryDate(LocalDate.of(2026, 12, 1));

        Card toCard = new Card();
        toCard.setId(2L);
        toCard.setCardNumber("123456789012");
        toCard.setUser(new User());
        toCard.setBalance(123L);
        toCard.setStatus(CardStatus.ACTIVE);
        toCard.setExpiryDate(LocalDate.of(2026, 12, 1));

        transactionsDTO.setAmount(100L);
        transactionsDTO.setDescription("123456789012");
        transactionsDTO.setToId(toCard.getId());
        transactionsDTO.setFromId(fromCard.getId());
        transactionsDTO.setToCardNumber(toCard.getCardNumber());

        Transaction transaction = new Transaction(transactionsDTO.getAmount(), transactionsDTO.getFromId(), toCard.getId(), transactionsDTO.getDescription());

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        assertTrue(cardService.transactions(transactionsDTO, fromCard, toCard) instanceof Transaction);
    }
}