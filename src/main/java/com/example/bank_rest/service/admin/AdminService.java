package com.example.bank_rest.service.admin;

import com.example.bank_rest.dto.auth.RegistrationAdminDTO;
import com.example.bank_rest.dto.auth.RegistrationDTO;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.RoleRepository;
import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    public User createNewAdmin(RegistrationAdminDTO registrationDTO){

        if(!registrationDTO.getCode().equals("5432")){
            return null;
        }

        User user = new User();

        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRoles(List.of(roleService.getAdminRole()));

        return userRepository.save(user);
    }
}
