package com.example.bank_rest.service.admin;

import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.util.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final Pagination pagination;

    public List<User> getUsers(int page, int size){
        return pagination.getPagination(userRepository.findAll(), page, size);
    }
}
