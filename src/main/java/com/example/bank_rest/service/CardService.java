package com.example.bank_rest.service;

import com.example.bank_rest.dto.card.CardDTO;
import com.example.bank_rest.dto.card.TransactionsDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.CardStatus;
import com.example.bank_rest.entity.Transaction;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;


    public List<CardDTO> getCards(int page, int size, User user){
        int left = (page-1)*size;
        int right = page*size-1;
        List<Card> cards = cardRepository.findByUser(user);

        if(cards.size() < right){
            right = cards.size();
            left = right-size-1;
        }

        if(left < 0){
            left = 0;
        }
        if(right == -1){
            return null;
        }

        return cards.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()).subList(left, right);
    }

    private CardDTO convertToResponse(Card card){
        CardDTO cardDTO = new CardDTO();

        cardDTO.setId(card.getId());
        cardDTO.setCardNumber(card.getCardNumber());
        cardDTO.setExpiryDate(card.getExpiryDate());
        cardDTO.setStatus(card.getStatus());
        cardDTO.setBalance(card.getBalance());

        return cardDTO;
    }

    public CardDTO responseBlockCard(Card card){
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);

        return convertToResponse(card);
    }

    public CardDTO getCardById(Card card){
        return convertToResponse(card);
    }


    @Transactional
    public Transaction transactions(TransactionsDTO transactionsDTO, Card fromCard, Card toCard){
        fromCard.setBalance(fromCard.getBalance() - transactionsDTO.getAmount());
        toCard.setBalance(toCard.getBalance() + transactionsDTO.getAmount());

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
        Transaction transaction = new Transaction(transactionsDTO.getAmount(), transactionsDTO.getFromId(), toCard.getId(), transactionsDTO.getDescription());

        return transactionRepository.save(transaction);
    }
}
