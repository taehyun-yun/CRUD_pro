package com.example.crud_project.controller;

import com.example.crud_project.config.auth.PrincipalDetails;
import com.example.crud_project.dao.UserDAO;
import com.example.crud_project.dto.User;
import com.example.crud_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDAO userdao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(
            Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails userDetails){ // DI(디펜더시 인젝션 한다.) 의존성 주입, 해당 어노테이션을 통해서 세션정보 확인 가능.
        System.out.println("/test/login =========");
        PrincipalDetails principalDetails= (PrincipalDetails)authentication.getPrincipal();
        System.out.println(" 현재 로그인 컨트롤러 authentication : "+ principalDetails.getUser());
        System.out.println("=============================================");
        System.out.println(" 현재 로그인 컨트롤러 userDetails : "+ userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(
            Authentication authentication){ // DI(디펜더시 인젝션 한다.) 의존성 주입, 해당 어노테이션을 통해서 세션정보 확인 가능.
        System.out.println("/test/oauth/login =========");
        OAuth2User oauth2User= (OAuth2User)authentication.getPrincipal();
        System.out.println("Oauth auth : "+ oauth2User.getAttributes());
        return "Oauth 세션 정보 확인하기";
    }

    // OAuth 로그인을 해도 PrincipalDetails
    // 일반 로그인을 해도 PrincipalDetails
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println(" 로그인 컨트롤러 /user 입니다.");
        System.out.println("principalDetails : "+ principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    // 로그인
    // 스프링 시큐리티가 해당 주소를 낚아챈다. -> SecurityConfig 파일 생성후 작동 안한다.
    @GetMapping("/loginForm")
    public String login(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    // 회원가입
    @PostMapping("/join")
    public String join(User user){
        System.out.println("로그인 컨트롤러(join 부분 ) : "+ user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userdao.save(user); // 비밀번호 암호화 완료.
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // admin 외 접근 거부
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터 정보";
    }


}// end class

