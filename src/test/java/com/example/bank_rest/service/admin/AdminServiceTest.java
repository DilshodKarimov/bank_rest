package com.example.bank_rest.service.admin;

import com.example.bank_rest.dto.auth.RegistrationAdminDTO;
import com.example.bank_rest.entity.Role;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdminServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    @Test
    void createAdmin() {
        RegistrationAdminDTO registrationAdminDTO = new RegistrationAdminDTO("Alex2", "123", "123");

        User user = new User();

        user.setId(1L);
        user.setUsername("Alex2");

        Role role = new Role();

        when(passwordEncoder.encode(registrationAdminDTO.getPassword())).thenReturn("123");
        when(roleService.getAdminRole()).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(user);


        assertTrue(adminService.createNewAdmin(registrationAdminDTO) instanceof User);
    }
}