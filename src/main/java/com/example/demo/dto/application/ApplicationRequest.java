package com.example.demo.dto.application;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationRequest {

    @NotNull(message = "Job id is required")
    private Long jobId;
}