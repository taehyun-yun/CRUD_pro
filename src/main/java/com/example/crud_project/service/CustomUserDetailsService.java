package com.example.crud_project.service;

import com.example.crud_project.config.CustomAuthority;
import com.example.crud_project.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    // CustomUserDetailsService 객체가 생성될 때 해당 객체에 대한 UserService 의 인스턴스가 자동으로 주입된다.
    // CustomUserDetailsService 클래스에서는 userService 변수를 사용하여 사용자 데이터에 접근할 수 있게 된다.
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자를 데이터베이스, 외부 소스 또는 다른 곳에서 가져와서 CustomUserDetailsDto 객체로 매핑
        // 예를 들어, 사용자의 정보를 데이터베이스에서 가져온다고 가정.

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username(유저가 유저이름을 찾지 못하였습니다.) : " + username);
        }

        // 사용자의 권한을 가져와서 GrantedAuthority 객체로 변환
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new CustomAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                // 해당 사용자가 가지고 있는 권한 정보를 사용하여 User 객체를 생성, 이 객체는 Spring Security 의 인증 및 권한 부여 기능에서 사용된다.
                user.getUsername(),
                user.getPassword(),
                authorities); // 사용자가 가지고 있는 권한 목록을 나타낸다.
    } // end Override

} // end class
