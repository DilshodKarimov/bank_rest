package com.example.bank_rest.controller;

import com.example.bank_rest.dto.auth.RegistrationAdminDTO;
import com.example.bank_rest.dto.auth.RegistrationDTO;
import com.example.bank_rest.dto.jwt.JwtRequestDTO;
import com.example.bank_rest.exception.AppError;
import com.example.bank_rest.service.AuthService;
import com.example.bank_rest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.example.bank_rest.dto.user.UserDTO;
import com.example.bank_rest.dto.jwt.JwtResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Tag(name = "AuthController", description = "Контроллер для регистрации и авторизации")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

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

        if(!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())){
            String message = "Пароли не совподают";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        if(userService.findByUsername(registrationDTO.getUsername()).isPresent()){
            String message = "Пользователь с таким именем существует!";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(authService.createUser(registrationDTO));
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

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequestDTO.getUsername(), jwtRequestDTO.getPassword()));
        }
        catch (BadCredentialsException e){
            String message = "Неправильный логин или пароль!";
            return  new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(),message), HttpStatus.UNAUTHORIZED);
        }
        catch (RuntimeException e){
            String message = "Неправильный логин или пароль!";
            return  new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(),message), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(authService.createAuthToken(jwtRequestDTO));
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

        if(!registrationAdminDTO.getPassword().equals(registrationAdminDTO.getConfirmPassword())){
            String message = "Пароли не совподают";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        if(userService.findByUsername(registrationAdminDTO.getUsername()).isPresent()){
            String message = "Пользователь с таким именем существует!";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        if(!registrationAdminDTO.getCode().equals("5432")){
            String message = "Мало прав!!!";
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), message), HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(authService.createAdmin(registrationAdminDTO));
    }
}
