package com.example.bank_rest.controller.admin;

import com.example.bank_rest.dto.card.CardDTO;
import com.example.bank_rest.dto.card.CardStatusDTO;
import com.example.bank_rest.dto.card.CreateCardDTO;
import com.example.bank_rest.dto.card.DeleteResponseDTO;
import com.example.bank_rest.exception.NotFoundException;
import com.example.bank_rest.service.admin.AdminCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/card")
@RequiredArgsConstructor
@Tag(name = "AdminController")
public class AdminController {

    private final AdminCardService adminCardService;

    /**
     * Возращает карты пользователя с пагинацией
     *
     * @param page - страница
     * @param size - максимальное количество карт в каждой странице
     * @return {@link ResponseEntity}, содержащий один из следующих результатов:
     * <ul>
     *   <li><b>200 OK</b> — если карты найдены </li>
     *   <li><b>400 Bad Request</b> — в случае если page и size заданы неправльно </li>
     * </ul>
     */
    @GetMapping("/")
    @Operation(description = "Возвращает все карты")
    public ResponseEntity<?> getCards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue =  "10") int size){
        return adminCardService.getCards(page, size);
    }


    /**
     * Данные карты
     *
     * @param id - id карты
     * @return <b>200 OK</b>
     * @throws NotFoundException - В случае если id не найден
     */
    @GetMapping("/{id}")
    @Operation(description = "Получает данные карты с поммощью айди")
    public ResponseEntity<?> getCardById(@PathVariable("id") Long id){
        return adminCardService.getCardById(id);
    }

    /**
     * Изменяет статус карты
     *
     * @param id - id карты
     * @param cardStatusDTO - статус карты на которую будет изменен
     * @return {@link ResponseEntity}, содержащий один из следующих результатов:
     * <ul>
     *   <li><b>200 OK</b> — успешно изменен статус, тело содержит {@link CardDTO}</li>
     *   <li><b>400 Bad Request</b> — в случае если не указана дата </li>
     * </ul>
     * @throws NotFoundException - В случае если карта не найдена
     */
    @PatchMapping("/{id}")
    @Operation(description = "Изменяет статус карты, если хотите продлить нужно указать дату окончания")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Long id, @RequestBody CardStatusDTO cardStatusDTO){
        return adminCardService.changeStatus(id, cardStatusDTO);
    }

    /**
     * Удалям карту
     *
     * @param id - id карты
     * @return <b>200 OK</b> — успешно удален, тело содержит {@link DeleteResponseDTO}
     * @throws NotFoundException - В случае если карта не найдена
     */
    @DeleteMapping("/{id}")
    @Operation(description = "Удаляет карту")
    public ResponseEntity<?> deleteCard(@PathVariable("id") Long id){
        return adminCardService.deleteCard(id);
    }


    /**
     * Возвращает пользователя карты
     *
     * @param id - id карты
     * @return <b>200 OK</b> — успешная работа и преносит на страницу пользователя
     * @throws NotFoundException - в случае если не найдена карта
     */
    @GetMapping("/{id}/user")
    @Operation(description = "Вернет пользователя с помощью айди карты")
    public ResponseEntity<?> getUserByCardId(@PathVariable("id") Long id){
        return adminCardService.getUserByCardId(id);
    }

    /**
     *
     * @param id - id карты
     * @param page - страница
     * @param size - максимальное количество карт в каждой странице
     * @return {@link ResponseEntity}, содержащий один из следующих результатов:
     * <ul>
     *   <li><b>200 OK</b> — если карты найдены </li>
     *   <li><b>400 Bad Request</b> — в случае если page и size заданы неправльно или  </li>
     * </ul>
     * @throws NotFoundException - в случае если id пользователя не найден!
     */
    @GetMapping("/user/{id}")
    @Operation(description = "Вернеет карты пользователя")
    public ResponseEntity<?> getCardByUserId(@PathVariable("id") Long id,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size){
        return adminCardService.getCardByUserId(id, page, size);
    }

    /**
     * Создает пользователю карту
     *
     * @param createCardDTO - данные чтобы создать карту
     * @return <b>200 OK</b> — если созадаст карту
     * @throws UsernameNotFoundException -  в случае если id пользователя не найден
     */
    @PostMapping("/create")
    @Operation(description = "Создает пользователю карту")
    public ResponseEntity<?> createCard(@RequestBody CreateCardDTO createCardDTO){
        return adminCardService.createCard(createCardDTO);
    }


}
