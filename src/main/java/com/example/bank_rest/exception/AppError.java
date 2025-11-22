package com.example.bank_rest.exception;


import lombok.Data;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Date;

@Data
public class AppError {

    private int status;
    private String message;
    private Date timestamp;

    public AppError(int status, String message){
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}


