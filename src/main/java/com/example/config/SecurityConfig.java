package com.example.config;

import com.example.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // パスワードを暗号化（ハッシュ化）するための部品
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ここでページのアクセス制限（ルール）を決めます
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // 1. ユーザー登録、ログイン画面、Bootstrapのスタイルシートは「誰でもアクセスOK」
                .requestMatchers("/register", "/login", "/webjars/**").permitAll()
                
                // 2. /admin/ から始まるURLは、管理者（ADMIN）権限を持っているユーザーだけOK
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // 3. それ以外のページ（書籍一覧など）は「ログイン（認証）が必要」
                .anyRequest().authenticated()
            )
            // ログインに関する設定
            .formLogin(form -> form
                .loginPage("/login")             // 自作のログイン画面のURL
                .defaultSuccessUrl("/books", true) // ログイン成功時の移動先
                .permitAll()
            )
            // ログアウトに関する設定
            .logout(logout -> logout
                .logoutSuccessUrl("/login")      // ログアウトした後の移動先
                .permitAll()
            )
            // 演習テキストに合わせ、一旦CSRFを無効化（※実践では有効にするのが基本です）
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // 認証マネージャーの設定（ログイン処理の裏側で使われます）
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
            .and()
            .build();
    }
}