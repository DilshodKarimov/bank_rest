package com.example.bank_rest.controller;

import com.example.bank_rest.dto.card.CardDTO;
import com.example.bank_rest.dto.card.TransactionsDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.CardStatus;
import com.example.bank_rest.exception.AppError;
import com.example.bank_rest.exception.NotFoundException;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card/transactions")
@RequiredArgsConstructor
@Tag(name = "TransactionController",description = "Работа с транзакциями")
public class TransactionController {

    private final CardRepository cardRepository;
    private final TransactionService transactions;

    /**
     * Переводит деньги на другую карту
     *
     * @param transactionsDTO - данные для транзакции
     * @return {@link ResponseEntity}, содержащий один из следующих результатов:
     * <ul>
     *   <li><b>200 OK</b> — успешно изменен статус, тело содержит {@link CardDTO}</li>
     *   <li><b>403 FORBIDDEN</b> — в случае если карта не принадлежит авторизованному пользователю </li>
     *   <li><b>410 Gone</b> — в случае если карта истекла </li>
     *   <li><b>423 Locked</b> — в случае если карта заблокирована </li>
     *   <li><b>402 PAYMENT REQUIRED</b> — в случае если на карте недостоточно средств </li>
     * </ul>
     * @throws NotFoundException - в случае если карта не найдена
     */
    @PostMapping()
    @Operation(description = "Перевод денег")
    public ResponseEntity<?> transactions(@RequestBody TransactionsDTO transactionsDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Card fromCard = cardRepository.findById(transactionsDTO.getFromId())
                .orElseThrow(() -> new NotFoundException("Карта с таким id не найден!"));

        Card toCard = cardRepository.findById(transactionsDTO.getToId())
                .orElseThrow(() -> new NotFoundException("Карта с таким id не найден!"));

        if(!fromCard.getUser().getUsername().equals(authentication.getName())){
            String message = "Недостаточно прав для доступа к ресурсу";
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), message), HttpStatus.FORBIDDEN);
        }

        if(fromCard.getStatus() != CardStatus.ACTIVE) {
            if (fromCard.getStatus() == CardStatus.BLOCKED) {
                String message = "Ваша карта заблокирована. Обратитесь в банк!";
                return new ResponseEntity<>(new AppError(HttpStatus.LOCKED.value(), message), HttpStatus.LOCKED);
            } else {
                String message = "Ваша карта истекла. Обратитесь в банк!";
                return new ResponseEntity<>(new AppError(HttpStatus.GONE.value(), message), HttpStatus.GONE);
            }
        }

        if(toCard.getStatus() != CardStatus.ACTIVE) {
            if (toCard.getStatus() == CardStatus.BLOCKED) {
                String message = "Карта к которую хотите отправить деньги заблокирована!";
                return new ResponseEntity<>(new AppError(HttpStatus.LOCKED.value(), message), HttpStatus.LOCKED);
            } else {
                String message = "Карта к которую хотите отправить деньги срок действия истек!";
                return new ResponseEntity<>(new AppError(HttpStatus.GONE.value(), message), HttpStatus.GONE);
            }
        }

        if(fromCard.getBalance() - transactionsDTO.getAmount() < 0){
            String message = "Недостоточно средств";
            return new ResponseEntity<>(new AppError(HttpStatus.PAYMENT_REQUIRED.value(), message), HttpStatus.PAYMENT_REQUIRED);
        }

        return ResponseEntity.ok(transactions.transactions(transactionsDTO, fromCard, toCard));
    }

}
