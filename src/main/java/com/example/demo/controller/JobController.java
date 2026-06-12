package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.JobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping
    public JobResponse createJob(
            @Valid
            @RequestBody JobRequest request,
            Authentication authentication
    ) {

        return jobService.createJob(
                request,
                authentication
        );
    }

    @GetMapping
    public Page<JobResponse> getAllJobs(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return jobService.getAllJobs(
                page,
                size
        );
    }

    @GetMapping("/{id}")
    public JobResponse getJob(
            @PathVariable Long id
    ) {

        return jobService.getJob(id);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PutMapping("/{id}")
    public JobResponse updateJob(
            @PathVariable Long id,
            @Valid
            @RequestBody JobRequest request
    ) {

        return jobService.updateJob(
                id,
                request
        );
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @DeleteMapping("/{id}")
    public void deleteJob(
            @PathVariable Long id
    ) {

        jobService.deleteJob(id);
    }
}