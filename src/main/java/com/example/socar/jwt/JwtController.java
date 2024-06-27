package com.example.socar.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    private JwtUtil jwtUtil;

//    @GetMapping("/generate")
//    public String generateToken(@RequestParam String username) {
//        return jwtUtil.generateToken(username);
//    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam String token, @RequestParam String username) {
        boolean isValid = jwtUtil.validateToken(token, username);
        return isValid ? "Valid token" : "Invalid token";
    }
}