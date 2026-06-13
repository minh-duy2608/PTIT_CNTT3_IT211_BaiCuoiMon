package com.example.demo.repository;

import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository
        extends JpaRepository<Job, Long> {

    List<Job> findByEmployer(User employer);

    void deleteByEmployer(User employer);
}