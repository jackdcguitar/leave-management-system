package com.example.leavesystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SPA 路由控制器
 * 用於支持 Vue Router 的 History 模式
 */
@Controller
public class SpaController {

    /**
     * 處理所有前端路由請求，轉發到 index.html
     * Vue Router 會接管後續的路由處理
     */
    @GetMapping(value = {"/", "/login", "/portal", "/portal/**"})
    public String spa() {
        return "forward:/index.html";
    }
}
