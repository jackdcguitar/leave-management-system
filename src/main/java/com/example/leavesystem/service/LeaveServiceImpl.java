package com.example.leavesystem.service;

import com.example.leavesystem.model.Leave;
import com.example.leavesystem.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;

    @Autowired
    public LeaveServiceImpl(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    @Override
    public void applyLeave(Leave leave) {
        // Basic validation
        if (leave.getStartDate() == null || leave.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        
        if (leave.getEndDate().isBefore(leave.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        
        if (leave.getLeaveType() == null || leave.getLeaveType().trim().isEmpty()) {
            throw new IllegalArgumentException("Leave type is required");
        }
        
        // Set default status if not set
        if (leave.getStatus() == null) {
            leave.setStatus("PENDING");
        }
        
        // Save the leave application
        leaveRepository.save(leave);
    }
    
    @Override
    public List<Leave> getLeavesByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        List<Leave> leaves = leaveRepository.findByUserId(userId);
        // Sort the leaves by startDate in descending order (newest first)
        leaves.sort((l1, l2) -> l2.getStartDate().compareTo(l1.getStartDate()));
        return leaves;
    }
}
