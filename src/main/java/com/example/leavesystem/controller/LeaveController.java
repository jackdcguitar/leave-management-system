package com.example.leavesystem.controller;

import com.example.leavesystem.model.Leave;
import com.example.leavesystem.entity.UserAccount;
import com.example.leavesystem.service.LeaveService;
import com.example.leavesystem.repo.UserAccountRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.GenericDeclaration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

class LeaveRequest {
    private String leaveType;
    private String startDate;
    private String endDate;
    private String reason;

    // Getters and Setters
    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }
    
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
@Controller
@RequestMapping("/view")
public class LeaveController {

    private final LeaveService leaveService;
    private final UserAccountRepository userAccountRepository;
    private static final Logger logger = LoggerFactory.getLogger(LeaveController.class);

    @Autowired
    public LeaveController(LeaveService leaveService, UserAccountRepository userAccountRepository) {
        this.leaveService = leaveService;
        this.userAccountRepository = userAccountRepository;
    }
    
    @GetMapping("/apply")
    public String view(@NotNull Model model, @NotNull Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("currentUser", username);
        model.addAttribute("leaveRequest", new LeaveRequest());
        
        // Get current user's leaves
        UserAccount user = userAccountRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        List<Leave> leaves = leaveService.getLeavesByUserId(user.getId());
        model.addAttribute("leaves", leaves);
        
        return "fragments/leave :: content";
    }

    @PostMapping("/leave/apply")
    public String applyLeave(@ModelAttribute("leaveRequest") LeaveRequest leaveRequest,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        try {
            // Parse the date-time strings to LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime startDate = LocalDateTime.parse(leaveRequest.getStartDate());
            LocalDateTime endDate = LocalDateTime.parse(leaveRequest.getEndDate());
            
            // Create a new Leave object and set its properties
            Leave leave = new Leave();
            leave.setStartDate(startDate);
            leave.setEndDate(endDate);
            String leaveType = leaveRequest.getLeaveType();
            logger.debug("Processing leave application with type: {}", leaveType);
            leave.setLeaveType(leaveType);
            leave.setReason(leaveRequest.getReason());

            // Set the user ID from the authenticated user
            UserAccount user = userAccountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
            leave.setUserId(user.getId());

            leaveService.applyLeave(leave);
            redirectAttributes.addFlashAttribute("successMessage", "Leave application submitted successfully!");
        } catch (Exception e) {
            logger.error("Error submitting leave application", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to submit leave application: " + e.getMessage());
        }
        return "redirect:/portal";
    }
}
