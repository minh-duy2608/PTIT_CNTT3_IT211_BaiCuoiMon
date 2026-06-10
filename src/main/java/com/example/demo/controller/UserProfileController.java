package com.example.demo.controller;

import com.example.demo.dto.user.UploadCvRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserRepository userRepository;

    @PutMapping("/cv")
    public String uploadCv(
            @RequestBody UploadCvRequest request,
            Authentication authentication
    ) {

        User user =
                userRepository
                        .findByEmail(
                                authentication.getName()
                        )
                        .orElseThrow();

        user.setCvUrl(
                request.getCvUrl()
        );

        userRepository.save(user);

        return "Upload CV success";
    }
}