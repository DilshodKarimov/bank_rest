package com.example.bank_rest.service;

import com.example.bank_rest.dto.auth.RegistrationAdminDTO;
import com.example.bank_rest.dto.auth.RegistrationDTO;
import com.example.bank_rest.dto.jwt.JwtRequestDTO;
import com.example.bank_rest.dto.jwt.JwtResponseDTO;
import com.example.bank_rest.dto.user.UserDTO;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.service.admin.AdminService;
import com.example.bank_rest.util.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AdminService adminService;


    @Test
    void createUser() {
        RegistrationDTO registrationDTO = new RegistrationDTO("Alex", "123","123");

        User user = new User();
        user.setUsername("Alex");
        user.setId(1L);

        when(userService.createNewUser(registrationDTO)).thenReturn(user);

        assertTrue(authService.createUser(registrationDTO) instanceof UserDTO);
    }

    @Test
    void createAuthToken() {
        JwtRequestDTO jwtRequestDTO = new JwtRequestDTO("Alex1", "123");
        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return null;
            }
        };

        when(userService.loadUserByUsername(jwtRequestDTO.getUsername())).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(any(UserDetails.class))).thenReturn("123");

        assertTrue(authService.createAuthToken(jwtRequestDTO) instanceof JwtResponseDTO);
    }

    @Test
    void createAdmin() {
        RegistrationAdminDTO registrationAdminDTO = new RegistrationAdminDTO("Alex2", "123", "123", "5432");

        User user = new User();

        user.setId(1L);
        user.setUsername("Alex2");

        when(adminService.createNewAdmin(registrationAdminDTO)).thenReturn(user);

        assertTrue(authService.createAdmin(registrationAdminDTO) instanceof UserDTO);
    }
}