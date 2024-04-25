package com.example.crud_project.config.auth;

import com.example.crud_project.dao.UserDAO;
import com.example.crud_project.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login) 으로 걸어 놨기 때문에
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 ioC 되어 있는 loadUserByUsername 함수가 실행.
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userdao;

    // 시큐리티 session 에 들어갈 수 있는건 Authentication 타입이고 Authentication 타입안에 UserDetails 타입이 들어와야 한다.
    // Security session (내부 Authentication (내부 UserDetails))
    @Override
    ////해당 코드의 String username 은 꼭 loginForm.html 의 input 타입의 name 이랑 일치 시켜야 할것!
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("PrincipalDetailsService 이다!! username = "+ username);
        User user = userdao.findByUsername(username);
        System.out.println("PrincipalDetailsService 이다!! user = "+ user);
        if (user == null){
            // 없다면
            return null;
        }
        // username 이 null 이 아니면 즉 유저가 있다면
        return new PrincipalDetails(user);
    }
}
