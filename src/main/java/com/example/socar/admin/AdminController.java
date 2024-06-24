package com.example.socar.admin;


import com.example.socar.car.Car;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Admin admin){
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        try {
            adminService.saveAdmin(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding car");
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Admin admin, HttpServletResponse response){
        try {
            Admin existingAdmin = adminService.getAdminByAdminId(admin.getAdminId());
            if (existingAdmin != null && passwordEncoder.matches(admin.getPassword(), existingAdmin.getPassword())) {
                Cookie cookie = new Cookie("success-token", "your-success-token-value");
                cookie.setHttpOnly(true); // XSS 공격 방지
                cookie.setPath("/"); // 쿠키의 유효 경로 설정
                response.addCookie(cookie);
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid admin ID or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login");
        }
    }

}
