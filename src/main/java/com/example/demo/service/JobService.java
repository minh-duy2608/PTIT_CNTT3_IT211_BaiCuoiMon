package com.example.demo.service;

import com.example.demo.dto.JobRequest;
import com.example.demo.dto.JobResponse;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobService {

    JobResponse createJob(
            JobRequest request,
            Authentication authentication
    );

    JobResponse updateJob(
            Long id,
            JobRequest request
    );

    Page<JobResponse> getAllJobs(
            int page,
            int size
    );

    JobResponse getJob(Long id);

    void deleteJob(Long id);
}