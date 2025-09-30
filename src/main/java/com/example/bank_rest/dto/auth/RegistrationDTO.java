package com.example.bank_rest.dto.auth;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String username;
    private String password;
    private String confirmPassword;
}
