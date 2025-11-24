package com.example.bank_rest.service.admin;

import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public List<User> getUsers(int page, int size){
        int left = (page-1)*size;
        int right = page*size-1;

        List<User> users = userRepository.findAll();

        if(users.size() < right){
            right = users.size();
            left = right-size-1;
        }

        if(left < 0){
            left = 0;
        }
        if(right == -1){
            return null;
        }

        return users.subList(left, right);
    }
}
