package com.example.crud_project.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import java.sql.*;
import java.util.Collection;

@NoArgsConstructor // 기본 생성자 생성
//@AllArgsConstructor // 모든 필드값을 파라미터로 받는 생성자 자동 생성, 클래스의 모든 필드를 한번에 초기화 가능
//@Setter
//@Getter
@Data
@Alias("User")
public class User implements UserDetails {

    // UserDetails 란?
    // Spring Security 에서 사용자의 정보를 담는 인터페이스이다.

    private int id;
    private String username;
    private String password;
    private String email;
    private String role;

    private String provider;
    private String providerId;

    private Collection<? extends GrantedAuthority> authorities; // 계정의 권한 목록을 리턴

    // Spring Security 에서 사용자의 정보를 불러오기 위해서 구현해야 하는 인터페이스로 기본적인 오버라이드 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    // 사용자가 만료되지 않은 경우를 반환
    @Override
    public boolean isAccountNonExpired() { // 계정의 만료 여부 리턴. 기본값 true(만료 안됨)
        return true;
    }

    // 사용자가 잠기지 않은 경우를 반환
    @Override
    public boolean isAccountNonLocked() { // 계정의 잠김 여부 리턴. 기본값 true(잠기지 않음)
        return true;
    }

    // 사용자의 자격 증명이 만료되지 않은 경우를 반환
    @Override
    public boolean isCredentialsNonExpired() { // 비밀번로 만료 여부 리턴. 기본값 true(만료 안됨)
        return true;
    }

    // 사용자가 활성 상태인 경우를 반환
    @Override
    public boolean isEnabled() { // 계정의 활성화 여부 리턴. 기본값 true(활성화 됨)
        return true;
    }

    @Builder
    public User(String username, String password, String email, String role, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}// end class
