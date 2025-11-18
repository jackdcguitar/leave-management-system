package com.example.leavesystem.controller;

import com.example.leavesystem.model.Leave;
import com.example.leavesystem.entity.UserAccount;
import com.example.leavesystem.service.LeaveService;
import com.example.leavesystem.repo.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LeaveRestController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @GetMapping
    public ResponseEntity<List<Leave>> getMyLeaves(Authentication authentication) {
        UserAccount user = userAccountRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

        List<Leave> leaves = leaveService.getLeavesByUserId(user.getId());
        return ResponseEntity.ok(leaves);
    }

    @PostMapping
    public ResponseEntity<?> applyLeave(@RequestBody Leave leave, Authentication authentication) {
        try {
            UserAccount user = userAccountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

            leave.setUserId(user.getId());
            leave.setStatus("PENDING");

            leaveService.applyLeave(leave);

            return ResponseEntity.ok(Map.of("success", true, "message", "Leave applied successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateLeaveStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        // TODO: Implement leave approval/rejection logic
        return ResponseEntity.ok(Map.of("success", true, "message", "Status updated"));
    }
}
