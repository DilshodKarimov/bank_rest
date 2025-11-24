package com.example.bank_rest.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationAdminDTO {
    private String username;
    private String password;
    private String confirmPassword;
    private String code;
}
