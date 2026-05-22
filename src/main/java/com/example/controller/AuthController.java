package com.example.controller;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 1. ユーザ作成画面を表示する
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    // 2. ユーザ作成のボタンが押されたときの処理
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, 
                               @RequestParam String password,
                               @RequestParam String role,
                               Model model) {
        try {
            // ユーザー登録を実行（重複があればここで例外が発生する）
            userService.registerUser(username, password, role);
        } catch (RuntimeException e) {
            // エラー（ユーザー名重複など）があったら、メッセージを画面に渡して登録画面に戻す
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        // 登録が成功したら、ログイン画面（/login）にリダイレクト（自動移動）する
        return "redirect:/login";
    }

    // 3. ログイン画面を表示する
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
}