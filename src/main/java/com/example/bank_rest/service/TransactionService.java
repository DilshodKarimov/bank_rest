package com.example.bank_rest.service;

import com.example.bank_rest.dto.card.TransactionsDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.Transaction;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

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
