package com.example.demo.dto.application;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplicationStatusUpdateRequest {

    @NotBlank(message = "Status is required")
    private String status;
}