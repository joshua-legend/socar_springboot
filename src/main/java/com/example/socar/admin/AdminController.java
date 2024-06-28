package com.example.socar.admin;


import com.example.socar.ApiResponse;
import com.example.socar.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdminController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminDetailsService adminDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/api/admin/register")
    public ResponseEntity<String> register(@RequestBody Admin admin){
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        try {
            adminService.saveAdmin(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding car");
        }
    }

    @PostMapping("/api/admin/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody Admin admin, HttpServletResponse response) {
        try {
            UserDetails userDetails = adminDetailsService.loadUserByUsername(admin.getAdminId());

            if (userDetails == null || !passwordEncoder.matches(admin.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid admin ID or password");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = JwtUtil.generateToken(admin);

            // JWT를 응답 헤더에 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwt);
            System.out.println(jwt);
            // JWT 토큰을 쿠키에 설정
//            Cookie cookie = new Cookie("jwt-token", jwt);
//            cookie.setHttpOnly(true); // JavaScript에서 접근 가능
//            cookie.setSecure(false); // HTTPS에서만 사용 (개발 중에는 필요에 따라 false로 설정)
//            cookie.setPath("/"); // 쿠키의 유효 경로 설정
//            cookie.setMaxAge(60); // 쿠키의 유효기간 설정 (예: 1분)
//            response.addCookie(cookie);

            ApiResponse<String> apiResponse = new ApiResponse<>("success", 200, 1, "Login successful");
            return ResponseEntity.ok().headers(headers).body(apiResponse);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            ApiResponse<String> apiResponse = new ApiResponse<>("error", 401, 0, "Invalid admin ID or password");
            return ResponseEntity.status(401).body(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>("error", 500, 0, "Error during login");
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    @PostMapping("/api/admin/cookie/login")
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
