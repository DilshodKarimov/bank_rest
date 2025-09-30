package com.example.bank_rest.dto.card;

import liquibase.datatype.DataTypeInfo;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateCardDTO {
    private LocalDate expired;
    private String username;
    private Long balance;
}
