package com.example.bank_rest.service;

import com.example.bank_rest.dto.card.TransactionsDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.CardStatus;
import com.example.bank_rest.entity.Transaction;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransactionService transactionService;

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

        Transaction transaction = new Transaction(transactionsDTO.getAmount(), transactionsDTO.getFromId(), toCard.getId(), transactionsDTO.getDescription());

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(cardRepository.save(any(Card.class))).thenReturn(toCard);

        assertTrue(transactionService.transactions(transactionsDTO, fromCard, toCard) instanceof Transaction);
    }
}