package com.example.demo.dto.application;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {

    private Long id;

    private String candidateName;

    private String jobTitle;

    private String status;
}