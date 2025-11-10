package com.example.bank_rest.service;

import com.example.bank_rest.dto.auth.RegistrationAdminDTO;
import com.example.bank_rest.dto.auth.RegistrationDTO;
import com.example.bank_rest.dto.jwt.JwtRequestDTO;
import com.example.bank_rest.dto.jwt.JwtResponseDTO;
import com.example.bank_rest.dto.user.UserDTO;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.AppError;
import com.example.bank_rest.service.admin.AdminService;
import com.example.bank_rest.service.admin.AdminUserService;
import com.example.bank_rest.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final AdminService adminService;

    public ResponseEntity<?> createUser(RegistrationDTO registrationDTO){

        if(!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())){
            String message = "Пароли не совподают";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        if(userService.findByUsername(registrationDTO.getUsername()).isPresent()){
            String message = "Пользователь с таким именем существует!";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        User user = userService.createNewUser(registrationDTO);

        return ResponseEntity.ok(new UserDTO(user.getId(), user.getUsername()));
    }

    public ResponseEntity<?> createAuthToken(JwtRequestDTO jwtRequestDTO){

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequestDTO.getUsername(), jwtRequestDTO.getPassword()));
        }
        catch (BadCredentialsException  e){
            String message = "Неправильный логин или пароль!";
            return  new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(),message), HttpStatus.UNAUTHORIZED);
        }
        catch (RuntimeException e){
            String message = "Неправильный логин или пароль!";
            return  new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(),message), HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userService.loadUserByUsername(jwtRequestDTO.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponseDTO(token));

    }

    public ResponseEntity<?> createAdmin(RegistrationAdminDTO registrationAdminDTO){
        if(!registrationAdminDTO.getPassword().equals(registrationAdminDTO.getConfirmPassword())){
            String message = "Пароли не совподают";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        if(userService.findByUsername(registrationAdminDTO.getUsername()).isPresent()){
            String message = "Пользователь с таким именем существует!";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        User user = adminService.createNewAdmin(registrationAdminDTO);
        if(user == null){
            String message = "Мало прав!!!";
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), message), HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(new UserDTO(user.getId(), user.getUsername()));
    }


}
