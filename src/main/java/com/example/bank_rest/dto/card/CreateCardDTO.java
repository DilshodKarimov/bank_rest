package com.example.bank_rest.dto.card;

import liquibase.datatype.DataTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardDTO {
    private String username;
    private LocalDate expired;
    private Long balance;
}
