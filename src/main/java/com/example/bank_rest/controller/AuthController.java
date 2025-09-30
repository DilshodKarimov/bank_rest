package com.example.bank_rest.controller;

import com.example.bank_rest.dto.auth.RegistrationAdminDTO;
import com.example.bank_rest.dto.auth.RegistrationDTO;
import com.example.bank_rest.dto.jwt.JwtRequestDTO;
import com.example.bank_rest.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Tag(name = "AuthController", description = "Контроллер для регистрации и авторизации")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    @Operation(summary = "Регистрация")
    public ResponseEntity<?> createUser(@RequestBody RegistrationDTO registrationDTO){
        return authService.createUser(registrationDTO);
    }

    @PostMapping("/auth")
    @Operation(summary = "Авторизация")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequestDTO jwtRequestDTO){
        return authService.createAuthToken(jwtRequestDTO);
    }

    @PostMapping("/registration-admin")
    @Operation(summary = "Создает админа, но нужно ввести код")
    public ResponseEntity<?> createUser(@RequestBody RegistrationAdminDTO registrationAdminDTO){
        return authService.createAdmin(registrationAdminDTO);
    }


}
