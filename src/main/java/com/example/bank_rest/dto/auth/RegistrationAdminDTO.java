package com.example.bank_rest.dto.auth;

import lombok.Data;

@Data
public class RegistrationAdminDTO {
    private String username;
    private String password;
    private String confirmPassword;
    private String code;
}
