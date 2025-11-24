package com.example.bank_rest.service;

import com.example.bank_rest.dto.auth.RegistrationDTO;
import com.example.bank_rest.dto.user.UserDTO;
import com.example.bank_rest.entity.Role;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.RoleRepository;
import com.example.bank_rest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserService userService;


    @Test
    void findByUsername() {
        assertFalse(userService.findByUsername("aidnoiwda").isPresent());
    }

    @Test
    void createNewUser() {
        RegistrationDTO registrationDTO = new RegistrationDTO("Alex", "123","123");

        Role role = new Role();
        role.setName("USER_ROLE");
        User user = new User();


        when(passwordEncoder.encode("123")).thenReturn("123");
        when(roleService.getUserRole()).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertTrue(userService.createNewUser(registrationDTO) instanceof User);
    }

    @Test
    void loadUserByUsername() {
        assertThrows(RuntimeException.class, () -> userService.loadUserByUsername("oaidnoawnd"));
    }


}