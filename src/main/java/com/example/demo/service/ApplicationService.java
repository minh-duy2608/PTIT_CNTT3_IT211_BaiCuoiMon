package com.example.demo.service;

import com.example.demo.dto.application.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ApplicationService {

    ApplicationResponse apply(
            ApplicationRequest request,
            Authentication authentication
    );

    List<ApplicationResponse> getAll();

    ApplicationResponse getById(Long id);

    ApplicationResponse updateStatus(
            Long id,
            ApplicationStatusUpdateRequest request
    );
}