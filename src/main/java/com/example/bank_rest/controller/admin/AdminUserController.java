package com.example.bank_rest.controller.admin;

import com.example.bank_rest.service.admin.AdminUserService;
import com.fasterxml.jackson.core.PrettyPrinter;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("/")
    @Operation(description = "Получает всех пользователей и админов")
    public ResponseEntity<?> getUsers(){
        return adminUserService.getUsers();
    }

    @GetMapping("/{id}")
    @Operation(description = "Переносит вас в /admin/card/user/")
    public String getUsersCard(@PathVariable("id") Long id){
        return "redirect:/admin/card/user/" + id.toString();
    }

}
