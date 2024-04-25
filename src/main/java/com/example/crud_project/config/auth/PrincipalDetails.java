package com.example.crud_project.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행 시킨다.
// 로그인을 진행이 완료가 되면 시큐리티 session 을 만들어 준다.(Security ContextHolder 라는 키값에 세션정보를 저장한다.)
// 오브젝트 -> Authentication 타입 객체여야만 한다.
// Authentication 안에 User 정보가 있어야 한다.
// User 오브젝트타입 -> UserDetails 타입 객체

import com.example.crud_project.dto.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// Security Session -> Authentication -> UserDetails(PrincipalDetails)
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // 컴포지션
    private Map<String, Object> attributes; // 프로필 정보를 통으로 Map 에 넣기 위해.

    // 일반 로그인시 사용하는 생성자
    public PrincipalDetails(User user){
        this.user = user;
    }

    // OAuth2 로그인시 사용하는 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 User 의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        // 해당 계정이 우리 사이트에서 1년동안 회원이 로그인을 안하면 휴면계정으로 하기로 했다! 하면 false 바꾸는 것.

        return true;
    }
    
    // 여기부터는 Oath2 오버라이드 함수
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }

    // 시큐리티 세션안에 들어갈 수 있는 타입은 Authentication 타입만 들어갈 수 있다.
    // Authentication 안에는 UserDetails, OAuth2User 두개의 필드가 있다.
    // 회원가입시 User 오브젝트가 필요.
    // UserDetails -> 일반 로그인 타입
    // OAuth2User -> OAth2 로그인 타입
    // 각각 쓰기 귀찮으니 PrincipalDetails 안에 묶어 버리자 해서 두개를 implements 한것
    // 그럼 그냥 PrincipalDetails 타입을 쓰면 묶여있는 두개의 타입안에 (User 오브젝트)을 편하게 사용할 수 있다.
}
