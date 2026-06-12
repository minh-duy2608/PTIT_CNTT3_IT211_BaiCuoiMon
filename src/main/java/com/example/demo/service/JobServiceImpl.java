package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl
        implements JobService {

    private final JobRepository jobRepository;

    private final UserRepository userRepository;

    @Override
    public JobResponse createJob(
            JobRequest request,
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

    @Override
    public Page<JobResponse> getAllJobs(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        return jobRepository
                .findAll(pageable)
                .map(this::map);
    }

    @Override
    public JobResponse getJob(Long id) {

        Job job =
                jobRepository
                        .findById(id)
                        .orElseThrow();

        return map(job);
    }

    @Override
    public JobResponse updateJob(
            Long id,
            JobRequest request
    ) {

        Job job =
                jobRepository
                        .findById(id)
                        .orElseThrow();

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());

        jobRepository.save(job);

        return map(job);
    }

    @Override
    public void deleteJob(Long id) {

        jobRepository.deleteById(id);
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