package com.example.bank_rest.dto.card;

import lombok.Data;

@Data
public class DeleteResponseDTO {
    private String status = "DELETED";
    private String message = "Карта успешно удалена";
}
