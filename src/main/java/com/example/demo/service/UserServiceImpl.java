package com.example.demo.service;

import com.example.demo.dto.user.UserDto;
import com.example.demo.dto.user.UserUpdateDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Job;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.RefreshTokenRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    private final JobRepository jobRepository;

    private final RefreshTokenRepository refreshTokenRepository;

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
    @Transactional
    public void delete(Long id) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("User not found")
                        );

        applicationRepository.deleteByCandidate(user);

        List<Job> jobs = jobRepository.findByEmployer(user);

        for (Job job : jobs) {
            applicationRepository.deleteByJob(job);
        }

        jobRepository.deleteByEmployer(user);

        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);

        userRepository.delete(user);
    }
}