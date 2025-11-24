package com.example.bank_rest.controller;

import com.example.bank_rest.dto.card.CardDTO;
import com.example.bank_rest.dto.card.TransactionsDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.CardStatus;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.AppError;
import com.example.bank_rest.exception.NotFoundException;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
@Tag(name = "CardController",description = "Работа с картами")
public class CardController {

    private final CardService cardService;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

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

        if(page <= 0 || size <= 0){
            String message = "Плохой запрос c пагинацией!";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user= userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        return ResponseEntity.ok(cardService.getCards(page, size, user));
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Карта с таким id не найден"));

        if(!card.getUser().getUsername().equals(authentication.getName())){
            String message = "Недостаточно прав для доступа к ресурсу";
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), message), HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(cardService.responseBlockCard(card));
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Карта с таким id не найден!"));

        if(!card.getUser().getUsername().equals(authentication.getName())){
            String message = "Недостаточно прав для доступа к ресурсу";
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), message), HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(cardService.getCardById(card));
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Card fromCard =  cardRepository.findById(transactionsDTO.getFromId())
                .orElseThrow(() -> new NotFoundException("Карта с таким id не найден!"));

        Card toCard;

        if (transactionsDTO.getToId() == -1) {
            toCard = cardRepository.findByCardNumber(transactionsDTO.getToCardNumber())
                    .orElseThrow(() -> new NotFoundException("Карта с таким номером не найден!"));
        } else {
            toCard = cardRepository.findById(transactionsDTO.getToId())
                    .orElseThrow(() -> new NotFoundException("Карта на id которую хотите перевести, не найден!"));
        }

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

        return ResponseEntity.ok(cardService.transactions(transactionsDTO, fromCard, toCard));
    }
}
