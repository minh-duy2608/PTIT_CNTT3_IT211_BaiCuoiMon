package com.example.demo.service;

import com.example.demo.dto.user.*;
import org.springframework.data.domain.Page;

public interface UserService {

    Page<UserDto> getAll(
            int page,
            int size,
            String keyword
    );

    UserDto getById(Long id);

    UserDto update(
            Long id,
            UserUpdateDto dto
    );

    void delete(Long id);
}