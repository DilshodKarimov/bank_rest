package com.example.bank_rest.service.admin;

import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.AppError;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;


    public ResponseEntity<?> getUsers(int page, int size){
        if(page <= 0 || size <= 0){
            String message = "Плохой запрос!";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        int left = (page-1)*size;
        int right = page*size-1;

        List<User> cards = userRepository.findAll();

        if(cards.size() < right){
            right = cards.size();
            left = right-size-1;
        }

        if(left < 0){
            left = 0;
        }
        if(right == -1){
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(cards.subList(left, right));
    }




}
