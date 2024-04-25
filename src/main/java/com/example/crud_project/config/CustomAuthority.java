package com.example.crud_project.config;

import org.springframework.security.core.GrantedAuthority;

import javax.management.relation.Role;

public class CustomAuthority implements GrantedAuthority {

    private final String role;

    public CustomAuthority(String  role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
