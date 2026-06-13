package com.example.demo.controller;

import com.example.demo.dto.user.*;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.response.MessageResponse;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public Page<UserDto> getAll(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "")
            String keyword
    ) {

        return userService.getAll(
                page,
                size,
                keyword
        );
    }

    @GetMapping("/{id}")
    public UserDto getById(
            @PathVariable Long id) {

        return userService.getById(id);
    }

    @PutMapping("/{id}")
    public UserDto update(
            @PathVariable Long id,
            @RequestBody UserUpdateDto dto) {

        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(
            @PathVariable Long id) {

        userService.delete(id);

        return MessageResponse.builder()
                .message("Delete user success")
                .build();
    }
}