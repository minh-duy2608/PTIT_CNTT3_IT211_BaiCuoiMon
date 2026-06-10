package com.example.demo.dto;

import lombok.*;

@Data
@Builder
public class JobResponse {

    private Long id;

    private String title;

    private String description;

    private String location;

    private Double salary;

    private boolean approved;

    private String employerName;
}