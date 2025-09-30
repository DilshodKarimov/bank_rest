package com.example.bank_rest.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtRequestDTO {
    private String username;
    private String password;
}
