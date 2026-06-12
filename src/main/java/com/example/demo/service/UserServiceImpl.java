package com.example.demo.service;

import com.example.demo.dto.user.UserDto;
import com.example.demo.dto.user.UserUpdateDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Page<UserDto> getAll(
            int page,
            int size,
            String keyword
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<User> users;

        if (keyword != null
                && !keyword.trim().isEmpty()) {

            users =
                    userRepository
                            .findByFullNameContainingIgnoreCase(
                                    keyword,
                                    pageable
                            );
        } else {

            users =
                    userRepository.findAll(
                            pageable
                    );
        }

        return users.map(user ->
                UserDto.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .enabled(user.getEnabled())
                        .build()
        );
    }

    @Override
    public UserDto getById(Long id) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException("User not found")
                        );

        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .build();
    }

    @Override
    public UserDto update(
            Long id,
            UserUpdateDto dto) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException("User not found")
                        );

        user.setFullName(dto.getFullName());
        user.setEnabled(dto.getEnabled());

        userRepository.save(user);

        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .build();
    }

    @Override
    public void delete(Long id) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException("User not found")
                        );

        userRepository.delete(user);
    }
}