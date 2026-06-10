package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @PostMapping
    public JobResponse createJob(
            @RequestBody JobRequest request,
            Authentication authentication
    ) {

        User employer =
                userRepository
                        .findByEmail(
                                authentication.getName()
                        )
                        .orElseThrow();

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .salary(request.getSalary())
                .approved(false)
                .createdAt(LocalDateTime.now())
                .employer(employer)
                .build();

        jobRepository.save(job);

        return map(job);
    }

    @GetMapping
    public List<JobResponse> getAllJobs() {

        return jobRepository
                .findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @GetMapping("/{id}")
    public JobResponse getJob(
            @PathVariable Long id
    ) {

        Job job =
                jobRepository
                        .findById(id)
                        .orElseThrow();

        return map(job);
    }

    private JobResponse map(Job job) {

        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .salary(job.getSalary())
                .approved(job.isApproved())
                .employerName(
                        job.getEmployer()
                                .getFullName()
                )
                .build();
    }
}