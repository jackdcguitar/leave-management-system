package com.example.leavesystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/overtime")
public class OvertimeController {

    @GetMapping("/view")
    public String view(Model model) {
        return "fragments/overtime :: content";
    }

    @PostMapping("/apply")
    public String apply(Model model) {
        return "fragments/overtime :: content";
    }
}
