package com.example.demo.controller;

import com.example.demo.dto.application.*;
import com.example.demo.entity.*;
import com.example.demo.enums.ApplicationStatus;
import com.example.demo.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationRepository applicationRepository;

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    @PostMapping
    public ApplicationResponse apply(
            @RequestBody ApplicationRequest request,
            Authentication authentication
    ) {

        User candidate =
                userRepository
                        .findByEmail(
                                authentication.getName()
                        )
                        .orElseThrow();

        Job job =
                jobRepository
                        .findById(request.getJobId())
                        .orElseThrow();

        Application application =
                Application.builder()
                        .candidate(candidate)
                        .job(job)
                        .status(ApplicationStatus.PENDING)
                        .appliedAt(LocalDateTime.now())
                        .build();

        applicationRepository.save(application);

        return map(application);
    }

    @GetMapping
    public List<ApplicationResponse> getAll() {

        return applicationRepository
                .findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @GetMapping("/{id}")
    public ApplicationResponse getById(
            @PathVariable Long id
    ) {

        Application application =
                applicationRepository
                        .findById(id)
                        .orElseThrow();

        return map(application);
    }

    @PutMapping("/{id}/status")
    public ApplicationResponse updateStatus(
            @PathVariable Long id,
            @RequestBody ApplicationStatusUpdateRequest request
    ) {

        Application application =
                applicationRepository
                        .findById(id)
                        .orElseThrow();

        application.setStatus(
                ApplicationStatus.valueOf(
                        request.getStatus()
                )
        );

        applicationRepository.save(application);

        return map(application);
    }

    private ApplicationResponse map(
            Application application
    ) {

        return ApplicationResponse.builder()
                .id(application.getId())
                .candidateName(
                        application
                                .getCandidate()
                                .getFullName()
                )
                .jobTitle(
                        application
                                .getJob()
                                .getTitle()
                )
                .status(
                        application
                                .getStatus()
                                .name()
                )
                .build();
    }
}