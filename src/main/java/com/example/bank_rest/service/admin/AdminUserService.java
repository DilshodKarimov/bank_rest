package com.example.bank_rest.service.admin;

import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;


    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }




}
