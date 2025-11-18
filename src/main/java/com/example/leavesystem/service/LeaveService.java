package com.example.leavesystem.service;

import com.example.leavesystem.model.Leave;
import java.util.List;

public interface LeaveService {
    void applyLeave(Leave leave);
    List<Leave> getLeavesByUserId(Long userId);
}
