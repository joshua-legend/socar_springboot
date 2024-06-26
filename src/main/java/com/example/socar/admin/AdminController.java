package com.example.socar.admin;


import com.example.socar.car.Car;
import com.example.socar.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")

public class AdminController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

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
    public ResponseEntity<String> login(@RequestBody Admin admin,HttpServletResponse response) {
        try {
            Admin existingAdmin = adminService.getAdminByAdminId(admin.getAdminId());
            if (existingAdmin != null && passwordEncoder.matches(admin.getPassword(), existingAdmin.getPassword())) {
                String token = jwtUtil.generateToken(admin.getAdminId());
                System.out.println(token);
                // JWT 토큰을 쿠키에 설정
                Cookie cookie = new Cookie("jwt-token", token);
                cookie.setHttpOnly(false); // XSS 공격 방지 true이면 자바스크립트로 확인 불가능함 ㅅㄱ
                cookie.setSecure(false); // HTTPS에서만 사용 (개발 중에는 필요에 따라 false로 설정)
                cookie.setPath("/"); // 쿠키의 유효 경로 설정
                cookie.setMaxAge(60); // 쿠키의 유효기간 설정 (예: 1분)
                response.addCookie(cookie);
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid admin ID or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login");
        }
    }



    @PostMapping("/cookie/login")
    public ResponseEntity<String> cookieLogin(@RequestBody Admin admin, HttpServletResponse response) {
        try {
            Admin existingAdmin = adminService.getAdminByAdminId(admin.getAdminId());
            if (existingAdmin != null && passwordEncoder.matches(admin.getPassword(), existingAdmin.getPassword())) {
                Cookie cookie = new Cookie("success-token", "your-success-token-value");
                cookie.setHttpOnly(false);
                cookie.setPath("/");
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
