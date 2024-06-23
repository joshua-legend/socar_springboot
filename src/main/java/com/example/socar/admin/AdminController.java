package com.example.socar.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody Admin admin){
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);

        // 암호화된 비밀번호 확인
        System.out.println(admin);
        return "test";
    }
}
