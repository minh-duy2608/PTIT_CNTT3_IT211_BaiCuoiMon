package com.example.demo.controller;

import com.example.demo.dto.application.*;
import com.example.demo.service.ApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping
    public ApplicationResponse apply(
            @Valid
            @RequestBody ApplicationRequest request,
            Authentication authentication
    ) {

        return applicationService.apply(
                request,
                authentication
        );
    }

    @GetMapping
    public List<ApplicationResponse> getAll() {

        return applicationService.getAll();
    }

    @GetMapping("/{id}")
    public ApplicationResponse getById(
            @PathVariable Long id
    ) {

        return applicationService.getById(id);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PutMapping("/{id}/status")
    public ApplicationResponse updateStatus(
            @PathVariable Long id,
            @Valid
            @RequestBody ApplicationStatusUpdateRequest request
    ) {

        return applicationService.updateStatus(
                id,
                request
        );
    }
}