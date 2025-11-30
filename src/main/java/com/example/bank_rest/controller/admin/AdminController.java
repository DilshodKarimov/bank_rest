package com.example.bank_rest.controller.admin;

import com.example.bank_rest.dto.auth.RegistrationAdminDTO;
import com.example.bank_rest.dto.user.UserDTO;
import com.example.bank_rest.exception.AppError;
import com.example.bank_rest.service.UserService;
import com.example.bank_rest.service.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    /**
     * Регистрирует Администратора
     *
     * @param registrationAdminDTO - данные для регистрации
     * @return {@link ResponseEntity}, содержащий один из следующих результатов:
     * <ul>
     *   <li><b>200 OK</b> — если пользователь найден, тело содержит {@link UserDTO}</li>
     *   <li><b>400 Bad Request</b> — в случае если пороли не совподают или имя пользователя уже занято</li>
     *   <li><b>403 FORBIDDEN</b> — в случае если код ввели неправильно</li>
     * </ul>
     */
    @PostMapping("/registration")
    @Operation(summary = "Создает админа")
    public ResponseEntity<?> createUser(@RequestBody RegistrationAdminDTO registrationAdminDTO){

        if(!registrationAdminDTO.getPassword().equals(registrationAdminDTO.getConfirmPassword())){
            String message = "Пароли не совподают";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        if(userService.findByUsername(registrationAdminDTO.getUsername()).isPresent()){
            String message = "Пользователь с таким именем существует!";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(adminService.createNewAdmin(registrationAdminDTO));
    }
}
