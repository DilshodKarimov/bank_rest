package com.example.bank_rest.controller;

import com.example.bank_rest.dto.card.TransactionsDTO;
import com.example.bank_rest.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
@Tag(name = "CardController",description = "Работа с картами")
public class CardController {

    private final CardService cardService;


    @GetMapping("/")
    @Operation(description = "Возвращает карты авторизированного пользователя")
    public ResponseEntity<?> getMyCards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){

        return cardService.getCards(page, size);
    }

    @PatchMapping("/{id}")
    @Operation(description = "Блокирует карту")
    public ResponseEntity<?> blockCard(@PathVariable("id") Long id){
        return cardService.responseBlockCard(id);
    }

    @GetMapping("/{id}")
    @Operation(description = "Получает данные карты")
    public ResponseEntity<?> getCardById(@PathVariable("id") Long id){
        return cardService.getCardById(id);
    }

    @PostMapping("/transactions")
    @Operation(description = "Перевод денег")
    public ResponseEntity<?> transactions(@RequestBody TransactionsDTO transactionsDTO){
        return cardService.transactions(transactionsDTO);
    }




}
