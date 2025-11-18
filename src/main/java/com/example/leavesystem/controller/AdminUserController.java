package com.example.leavesystem.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @GetMapping("/view")
    public String view(Model model) {
        return "fragments/users :: content";
    }

    @PostMapping("/create")
    public String create(Model model) {
        return "fragments/users :: content";
    }
}
