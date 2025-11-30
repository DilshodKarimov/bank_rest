package com.example.bank_rest.service.admin;

import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.util.Pagination;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdminUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private Pagination pagination;

    @InjectMocks
    private AdminUserService adminUserService;

    @Test
    void getUsers() {
        List<User> userList = List.of(
                new User(1L, "Alex"),
                new User(2L, "Alex2"),
                new User(3L, "Alex3")
        );
        List<User> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);
        when(pagination.<User>getPagination(anyList(), anyInt(), anyInt()))
                .thenReturn(userList);

        List<User> result = adminUserService.getUsers(1, 10);

        assertFalse(result.isEmpty());
        assertTrue(result instanceof List<User>);
    }
}
