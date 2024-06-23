package com.example.socar.admin;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @PostMapping("/register")
    public String register(@RequestBody Admin admin){
        System.out.println(admin);
        return "test";
    }
}
