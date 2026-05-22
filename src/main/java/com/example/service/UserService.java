package com.example.service;

import com.example.model.User; // ここをUserに変更
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(String username, String password, String role) {
        // 1. ユーザー名の重複チェック
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("ユーザー名は既に使用されています");
        }

        // 2. パスワードを安全にハッシュ化（暗号化）
        String encodedPassword = passwordEncoder.encode(password);

        // 3. 暗号化したパスワードと権限（ROLE）を設定して保存
        User user = new User(username, encodedPassword, role); // ここをUserに変更
        userRepository.save(user);
    }
}