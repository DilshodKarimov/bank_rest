package com.example.bank_rest.controller.admin;

import com.example.bank_rest.exception.AppError;
import com.example.bank_rest.service.admin.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;


    /**
     * Возвращает всех пользователей
     *
     * @param page - страница
     * @param size - максимальное количество карт в каждой странице
     * @return {@link ResponseEntity}, содержащий один из следующих результатов:
     * <ul>
     *   <li><b>200 OK</b> — Возращает всех пользоватлей </li>
     *   <li><b>400 Bad Request</b> — в случае если page и size заданы неправльно </li>
     * </ul>
     */
    @GetMapping("/")
    @Operation(description = "Получает всех пользователей и админов")
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue =  "10") int size){
        if(page <= 0 || size <= 0){
            String message = "Плохой запрос!";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(adminUserService.getUsers(page, size));
    }

    /**
     * Переносит на другую страницу и покажет все карты пользоватля
     *
     * @param id - id пользоватедя
     * @return - Переносит на другую страницу
     */
    @GetMapping("/{id}")
    @Operation(description = "Переносит вас в /admin/card/user/")
    public ResponseEntity<String> getUsersCard(@PathVariable("id") Long id){
        return ResponseEntity.ok("redirect:/admin/card/user/" + id.toString());
    }

}
