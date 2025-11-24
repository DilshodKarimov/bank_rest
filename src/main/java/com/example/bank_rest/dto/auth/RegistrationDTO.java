package com.example.bank_rest.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationDTO {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
}
