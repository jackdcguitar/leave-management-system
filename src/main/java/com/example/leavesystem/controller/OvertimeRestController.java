package com.example.leavesystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/overtimes")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OvertimeRestController {

    @GetMapping
    public ResponseEntity<List<?>> getMyOvertimes(Authentication authentication) {
        // TODO: Implement overtime retrieval logic
        // For now, return empty list
        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping
    public ResponseEntity<?> applyOvertime(@RequestBody Map<String, String> request, Authentication authentication) {
        // TODO: Implement overtime application logic
        return ResponseEntity.ok(Map.of("success", true, "message", "Overtime applied successfully"));
    }
}
