package com.example.bank_rest.service;

import com.example.bank_rest.dto.card.CardDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.CardStatus;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.TransactionRepository;
import com.example.bank_rest.util.Pagination;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
class CardServiceTest {
    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Pagination pagination;

    @InjectMocks
    private CardService cardService;

    @Test
    void getCards() {
        User user = new User();

        List<Card> cards = new ArrayList<>();

        when(cardRepository.findByUser(user)).thenReturn(cards);
        when(pagination.<Card>getPagination(anyList(), anyInt(), anyInt())).thenReturn(cards);

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

        assertTrue(cardService.blockCard(card) instanceof CardDTO);
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

        assertTrue(cardService.getCard(card) instanceof CardDTO);
    }
}