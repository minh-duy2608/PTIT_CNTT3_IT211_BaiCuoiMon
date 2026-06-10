package com.example.demo.dto;

import lombok.Data;

@Data
public class JobRequest {

    private String title;

    private String description;

    private String location;

    private Double salary;
}