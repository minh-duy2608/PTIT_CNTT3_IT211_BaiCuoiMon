package com.example.demo.config;

import com.example.demo.entity.Role;
import com.example.demo.enums.RoleName;
import com.example.demo.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles() {

        return args -> {

            if(roleRepository.count() == 0){

                roleRepository.save(
                        Role.builder()
                                .name(RoleName.ROLE_ADMIN)
                                .build()
                );

                roleRepository.save(
                        Role.builder()
                                .name(RoleName.ROLE_EMPLOYER)
                                .build()
                );

                roleRepository.save(
                        Role.builder()
                                .name(RoleName.ROLE_CANDIDATE)
                                .build()
                );
            }

        };
    }
}