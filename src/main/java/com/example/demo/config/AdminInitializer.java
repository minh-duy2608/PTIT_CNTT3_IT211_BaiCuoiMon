package com.example.demo.config;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.enums.RoleName;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner createAdmin() {

        return args -> {

            if (!userRepository.existsByEmail("admin@gmail.com")) {

                Role adminRole =
                        roleRepository
                                .findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow();

                User admin =
                        User.builder()
                                .fullName("System Admin")
                                .email("admin@gmail.com")
                                .password(
                                        passwordEncoder.encode("123456")
                                )
                                .enabled(true)
                                .roles(Set.of(adminRole))
                                .build();

                userRepository.save(admin);
            }
        };
    }
}