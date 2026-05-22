package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin") // このコントローラー内のメソッドはすべてURLが「/admin」から始まります
public class AdminController {

    // 管理者専用ダッシュボード画面を表示する
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard"; // templates/admin/dashboard.html を探す
    }
}