package com.example.bank_rest.repository;

import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CardRepository extends CrudRepository<Card, Long> {
    Optional<Card> findByCardNumber(String cardNumber);
    List<Card> findByUser(User user);
    List<Card> findAll();

}
