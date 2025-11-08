package com.example.bank_rest.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "description")
    private String description;

    @Column(name = "from_card_id")
    private Long fromCardId;

    @Column(name = "to_card_id")
    private Long toCardId;

    public Transaction(Long amount, Long fromCardId, Long toCardId, String description){
        this.amount = amount;
        this.fromCardId = fromCardId;
        this.toCardId = toCardId;
        this.description = description;
        this.createdAt = LocalDateTime.now();

    }


}
