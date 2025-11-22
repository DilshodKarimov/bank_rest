package com.example.bank_rest.controller;

import com.example.bank_rest.dto.card.CardDTO;
import com.example.bank_rest.dto.card.TransactionsDTO;
import com.example.bank_rest.exception.NotFoundException;
import com.example.bank_rest.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
@Tag(name = "CardController",description = "Работа с картами")
public class CardController {

    private final CardService cardService;


    /**
     * Возращает карты пользователя с пагинацией
     *
     * @param page - страница
     * @param size - максимальное количество карт в каждой странице
     * @return {@link ResponseEntity}, содержащий один из следующих результатов:
     * <ul>
     *   <li><b>200 OK</b> — если карты найдены, тело содержит {@link CardDTO}</li>
     *   <li><b>400 Bad Request</b> — в случае если page и size заданы неправльно </li>
     * </ul>
     * @throws UsernameNotFoundException - если пользователь не найден!
     */
    @GetMapping("/")
    @Operation(description = "Возвращает карты авторизированного пользователя")
    public ResponseEntity<?> getMyCards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){

        return cardService.getCards(page, size);
    }

    /**
     * Блокирует карту
     *
     * @param id - id карты
     * @return {@link ResponseEntity}, содержащий один из следующих результатов:
     * <ul>
     *   <li><b>200 OK</b> — успешно изменен статус, тело содержит {@link CardDTO}</li>
     *   <li><b>403 FORBIDDEN</b> — в случае если карта не принадлежит авторизованному пользователю </li>
     * </ul>
     * @throws NotFoundException - в случае если карту с таким id не найден
     */
    @PatchMapping("/{id}")
    @Operation(description = "Блокирует карту")
    public ResponseEntity<?> blockCard(@PathVariable("id") Long id){
        return cardService.responseBlockCard(id);
    }

    /**
     * Данные карты
     *
     * @param id - id карты
     * @return {@link ResponseEntity}, содержащий один из следующих результатов:
     * <ul>
     *   <li><b>200 OK</b> — успешно изменен статус, тело содержит {@link CardDTO}</li>
     *   <li><b>403 FORBIDDEN</b> — в случае если карта не принадлежит авторизованному пользователю </li>
     * </ul>
     * @throws NotFoundException - в случае если карту с таким id не найден
     */
    @GetMapping("/{id}")
    @Operation(description = "Получает данные карты")
    public ResponseEntity<?> getCardById(@PathVariable("id") Long id){
        return cardService.getCardById(id);
    }


    /**
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
    @PostMapping("/transactions")
    @Operation(description = "Перевод денег")
    public ResponseEntity<?> transactions(@RequestBody TransactionsDTO transactionsDTO){
        return cardService.transactions(transactionsDTO);
    }
}
