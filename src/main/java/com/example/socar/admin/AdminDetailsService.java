package com.example.socar.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    // 생성자 주입 사용
    @Autowired
    public AdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByAdminId(username);
        System.out.println("adminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadminadmin");
        if(admin == null) throw new UsernameNotFoundException("그런 유저 없음");
        return new User(admin.getAdminId(), admin.getPassword(), Collections.emptyList());
    }
}
