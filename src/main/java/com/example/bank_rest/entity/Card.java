package com.example.bank_rest.entity;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", length = 12, unique = true)
    private String cardNumber;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    @Column(name = "balance")
    private Long balance;


    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    private User user;

    @PreUpdate
    private void updateExpiredStatus(){
        if(this.expiryDate != null && this.status  != CardStatus.BLOCKED){
            if(this.expiryDate.isBefore(LocalDate.now())){
                this.status = CardStatus.EXPIRED;
            }
        }
    }

//    @PrePersist
//    private void createCardNumber(){
//        String a = this.id.toString();
//        String zero = a.repeat(12-a.length());
//        this.cardNumber = zero+a;
//    }


}




//       /\ /\
//      ( *  *)
