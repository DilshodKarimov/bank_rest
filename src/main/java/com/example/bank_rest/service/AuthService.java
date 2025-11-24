package com.example.bank_rest.service;

import com.example.bank_rest.dto.auth.RegistrationAdminDTO;
import com.example.bank_rest.dto.auth.RegistrationDTO;
import com.example.bank_rest.dto.jwt.JwtRequestDTO;
import com.example.bank_rest.dto.jwt.JwtResponseDTO;
import com.example.bank_rest.dto.user.UserDTO;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.service.admin.AdminService;
import com.example.bank_rest.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AdminService adminService;

    public UserDTO createUser(RegistrationDTO registrationDTO){
        User user = userService.createNewUser(registrationDTO);

        return new UserDTO(user.getId(), user.getUsername());
    }

    public JwtResponseDTO createAuthToken(JwtRequestDTO jwtRequestDTO){
        UserDetails userDetails = userService.loadUserByUsername(jwtRequestDTO.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);

        return new JwtResponseDTO(token);
    }

    public UserDTO createAdmin(RegistrationAdminDTO registrationAdminDTO){
        User user = adminService.createNewAdmin(registrationAdminDTO);

        return new UserDTO(user.getId(), user.getUsername());
    }
}
