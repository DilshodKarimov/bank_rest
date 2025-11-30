package com.example.bank_rest.service;

import com.example.bank_rest.dto.card.CardDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.CardStatus;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.util.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final Pagination pagination;

    public List<CardDTO> getCards(int page, int size, User user){
        List<Card> cards = cardRepository.findByUser(user);

        List<Card> result = pagination.getPagination(cards, page, size);


        return result.stream()
                .map(CardDTO::convertToResponse)
                .collect(Collectors.toList());
    }

    public CardDTO blockCard(Card card){
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);

        return CardDTO.convertToResponse(card);
    }

    public CardDTO getCard(Card card){
        return CardDTO.convertToResponse(card);
    }



}
