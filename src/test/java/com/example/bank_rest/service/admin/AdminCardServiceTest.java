package com.example.bank_rest.service.admin;

import com.example.bank_rest.dto.card.CardDTO;
import com.example.bank_rest.dto.card.CreateCardDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.CardStatus;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.NotFoundException;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.util.Pagination;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdminCardServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Pagination pagination;

    @InjectMocks
    private AdminCardService adminCardService;

    @Test
    void getCards() {
        List<Card> cards = new ArrayList<>();

        Card card1 = new Card();
        card1.setId(2L);
        card1.setCardNumber("123456789012");
        card1.setUser(new User());
        card1.setBalance(123L);
        card1.setStatus(CardStatus.ACTIVE);
        card1.setExpiryDate(LocalDate.of(2026, 12, 1));

        Card card2 = new Card();
        card2.setId(1L);
        card2.setCardNumber("123456789012");
        card2.setUser(new User());
        card2.setBalance(123L);
        card2.setStatus(CardStatus.ACTIVE);
        card2.setExpiryDate(LocalDate.of(2026, 12, 1));

        cards.add(card1);
        cards.add(card2);

        when(cardRepository.findAll()).thenReturn(cards);
        when(pagination.<Card>getPagination(anyList(), anyInt(), anyInt())).thenReturn(cards);

        assertFalse(adminCardService.getCards(1, 10).isEmpty());
        assertTrue(adminCardService.getCards(1, 10) instanceof List<Card>);
    }

    @Test
    void getCardById() {
        Card card1 = new Card();

        card1.setId(2L);
        card1.setCardNumber("123456789012");
        card1.setUser(new User());
        card1.setBalance(123L);
        card1.setStatus(CardStatus.ACTIVE);
        card1.setExpiryDate(LocalDate.of(2026, 12, 1));

        when(cardRepository.findById(card1.getId())).thenReturn(Optional.of(card1));

        assertTrue(adminCardService.getCardById(card1.getId()) instanceof Card);
        assertThrows(NotFoundException.class, () -> adminCardService.getCardById(100L));
    }

    @Test
    void getUserByCardId() {
        Card card1 = new Card();

        User user = new User();
        user.setUsername("Alex");
        user.setId(1L);

        card1.setId(2L);
        card1.setCardNumber("123456789012");
        card1.setUser(user);
        card1.setBalance(123L);
        card1.setStatus(CardStatus.ACTIVE);
        card1.setExpiryDate(LocalDate.of(2026, 12, 1));

        when(cardRepository.findById(card1.getId())).thenReturn(Optional.of(card1));

        assertThrows(NotFoundException.class, () -> adminCardService.getUserByCardId(100L));
        assertTrue(adminCardService.getUserByCardId(card1.getId()) instanceof String);
    }

    @Test
    void getCardByUserId() {
        User user = new User();
        user.setUsername("Alex");
        user.setId(1L);

        List<Card> cards = new ArrayList<>();

        Card card1 = new Card();
        card1.setId(2L);
        card1.setCardNumber("123456789012");
        card1.setUser(user);
        card1.setBalance(123L);
        card1.setStatus(CardStatus.ACTIVE);
        card1.setExpiryDate(LocalDate.of(2026, 12, 1));

        Card card2 = new Card();
        card2.setId(1L);
        card2.setCardNumber("123456789012");
        card2.setUser(user);
        card2.setBalance(123L);
        card2.setStatus(CardStatus.ACTIVE);
        card2.setExpiryDate(LocalDate.of(2026, 12, 1));

        cards.add(card1);
        cards.add(card2);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(cardRepository.findByUser(user)).thenReturn(cards);
        when(pagination.<Card>getPagination(anyList(), anyInt(), anyInt())).thenReturn(cards);

        assertTrue(adminCardService.getCardsByUserId(user.getId(), 1, 10) instanceof List<Card>);
        assertThrows(NotFoundException.class, () -> adminCardService.getCardsByUserId(123L, 1, 10));
    }
}