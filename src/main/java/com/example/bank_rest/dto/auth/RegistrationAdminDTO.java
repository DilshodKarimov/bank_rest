package com.example.bank_rest.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationAdminDTO {
    private String username;
    private String password;
    private String confirmPassword;
}
