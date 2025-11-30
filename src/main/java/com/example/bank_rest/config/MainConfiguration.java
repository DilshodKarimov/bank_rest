package com.example.bank_rest.config;

import com.example.bank_rest.entity.User;
import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.service.RoleService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class MainConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank RestAPI")
                        .description("Документация API для Bank rest")
                );
    }

    @Bean
    public CommandLineRunner createAdmin(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        return args -> {
            // Проверяем, не существует ли уже админа
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(List.of(roleService.getAdminRole()));

                userRepository.save(admin);
                System.out.println("✔ Администратор создан: admin/admin123");
            }
        };
    }

}

