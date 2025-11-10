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
import com.example.bank_rest.dto.user.UserDTO;
import com.example.bank_rest.dto.jwt.JwtResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Tag(name = "AuthController", description = "Контроллер для регистрации и авторизации")
public class AuthController {

    private final AuthService authService;

    /**
     * Регистрирует пользоватля
     *
     * @param registrationDTO - данныее для регистрации
     * @return {@link ResponseEntity}, содержащий один из следующих результатов:
     * <ul>
     *   <li><b>200 OK</b> — если пользователь найден, тело содержит {@link UserDTO}</li>
     *   <li><b>400 Bad Request</b> — в случае если пороли не совподают или имя пользователя уже занято</li>
     * </ul>
     */
    @PostMapping("/registration")
    @Operation(summary = "Регистрация")
    public ResponseEntity<?> createUser(@RequestBody RegistrationDTO registrationDTO){
        return authService.createUser(registrationDTO);
    }


    /**
    * Авторизация пользователей
    *
    * @param jwtRequestDTO - данныее для (авторизации) получения токена
    * @return {@link ResponseEntity}, содержащий один из следующих результатов:
    * <ul>
    *   <li><b>200 OK</b> — при успешной авторизации вернет токен {@link JwtResponseDTO}</li>
    *   <li><b>400 Bad Request</b> — в случае если неправильный логин или пароль </li>
    * </ul>
    * */
    @PostMapping("/auth")
    @Operation(summary = "Авторизация")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequestDTO jwtRequestDTO){
        return authService.createAuthToken(jwtRequestDTO);
    }

    /**
     * Регистрирует пользоватля
     *
     * @param registrationAdminDTO - данные для регистрации
     * @return {@link ResponseEntity}, содержащий один из следующих результатов:
     * <ul>
     *   <li><b>200 OK</b> — если пользователь найден, тело содержит {@link UserDTO}</li>
     *   <li><b>400 Bad Request</b> — в случае если пороли не совподают или имя пользователя уже занято</li>
     *   <li><b>403 FORBIDDEN</b> — в случае если код ввели неправильно</li>
     * </ul>
     */
    @PostMapping("/registration-admin")
    @Operation(summary = "Создает админа, но нужно ввести код")
    public ResponseEntity<?> createUser(@RequestBody RegistrationAdminDTO registrationAdminDTO){
        return authService.createAdmin(registrationAdminDTO);
    }


}
