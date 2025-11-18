package com.example.leavesystem.repository;

import com.example.leavesystem.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByUserId(Long userId);
}
