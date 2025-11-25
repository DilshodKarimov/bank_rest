package com.example.bank_rest.service.admin;

import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminUserService adminUserService;

    @Test
    void getUsers() {
        List<User> userList = new ArrayList<>();

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("Alex");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("Alex2");

        User user3 = new User();
        user3.setId(3L);
        user3.setUsername("Alex3");

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        when(userRepository.findAll()).thenReturn(userList);

        assertTrue(adminUserService.getUsers(1, 10) instanceof List<User>);
        assertFalse(adminUserService.getUsers(1, 10).isEmpty());
    }
}