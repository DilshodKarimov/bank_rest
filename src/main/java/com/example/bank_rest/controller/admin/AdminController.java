package com.example.bank_rest.controller.admin;

import com.example.bank_rest.dto.card.CardStatusDTO;
import com.example.bank_rest.dto.card.CreateCardDTO;
import com.example.bank_rest.service.admin.AdminCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/card")
@RequiredArgsConstructor
@Tag(name = "AdminController")
public class AdminController {

    private final AdminCardService adminCardService;

    @GetMapping("/")
    @Operation(description = "Возвращает все карты")
    public ResponseEntity<?> getCards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue =  "10") int size){
        return adminCardService.getCards(page, size);
    }

    @GetMapping("/{id}")
    @Operation(description = "Получает данные карты с поммощью айди")
    public ResponseEntity<?> getCardById(@PathVariable("id") Long id){
        return adminCardService.getCardById(id);
    }

    @PatchMapping("/{id}")
    @Operation(description = "Изменяет статус карты, если хотите продлить нужно указать дату окончания")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Long id, CardStatusDTO cardStatusDTO){
        return adminCardService.changeStatus(id, cardStatusDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Удаляет карту")
    public ResponseEntity<?> deleteCard(@PathVariable("id") Long id){
        return adminCardService.deleteCard(id);
    }

    @GetMapping("/{id}/user")
    @Operation(description = "Вернет пользователя с помощью айди карты")
    public String getUserByCardId(@PathVariable("id") Long id){
        return "redirect:/admin/user/" + adminCardService.getUserByCardId(id);
    }

    @GetMapping("/user/{id}")
    @Operation(description = "Вернеет карты пользователя")
    public ResponseEntity<?> getCardByUserId(@PathVariable("id") Long id,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size){
        return adminCardService.getCardByUserId(id, page, size);
    }

    @PostMapping("/create")
    @Operation(description = "Создает пользователю карту")
    public ResponseEntity<?> createCard(@RequestBody CreateCardDTO createCardDTO){
        return adminCardService.createCard(createCardDTO);
    }


}
