package com.example.crud_project.config;

import com.example.crud_project.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity // 해당 어노테이션이 Security 활성화 / 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
//@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured, prePost 어노테이션 활성화

public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    // WebSecurityConfigurerAdapter 해당 방식을 많이 사용하지만 해당 방식은 스프링에서 더이상 지원하지 않기 때문에 이러한 코드를 사용


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //.csrf(AbstractHttpConfigurer::disable);
                .csrf((csrfConfig) ->
                        csrfConfig.disable()
                )
                //.authorizeRequests()
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                // authorizeRequests는 특정 리소스(URL)의 특정 권한을 설정한다.
                // 스프링 시큐리티 5.8버전 이상에서는 antMatchers, mvcMatchers, regexMatchers가 더이상 사용되지 않기 때문에 requestMatchers를 사용해야 한다.
                .requestMatchers("/user/**").authenticated() // 해당주소로 들어갈때 인증이 필요하다. 라는 코드
                //.requestMatchers("/manager/**").authenticated() // 해당주소로 들어갈때 인증이 필요하다. 라는 코드
                .requestMatchers("/manager/**").access(new WebExpressionAuthorizationManager("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")) // 해당 주소는 인증및 해당 권한을 가지고 있는 사람만 들어갈수 있다.
                // .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and
                // hasRole('ROLE_USER')")
                .requestMatchers("/admin/**").access(new WebExpressionAuthorizationManager("hasRole('ROLE_ADMIN')"))
                .anyRequest().permitAll()
                )
                //.and() 해당 코드 사용 x
                .formLogin((formLogin)->
                        formLogin
                                .loginPage("/loginForm")
                                .loginProcessingUrl("/login") // login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해 준다.(컨트롤러에 login 을 안만들어도 된다.)
                                .defaultSuccessUrl("/")
                        )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                        .loginPage("/loginForm")
                                        .defaultSuccessUrl("/", true) // 구글 로그인이 완료된 뒤의 후처리가 필요
                                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                                        .userService(principalOauth2UserService)
                                        // Tip. 코드 x, (엑세스 토큰 + 사용자 프로필 정보 o)
                                        // 1. 코드 받기(인증 완료)
                                        // 2. 코드를 통해서 엑세스 토큰 받음(사용자 정보에 접근 권한 생긴다.)
                                        // 3. 사용자 프로필 정보를 가져와서
                                        // 4. 그 정보를 토대로 회원가입을 자동으로 진행시키키도 하고
                                        // 4-1 또는 정보가 부족하다
                                        // ex) 쇼핑몰인데 집주소가 정보에 없다 하면 추가정보를 받기위해 추가적인 회원가입창을 띄운다.

                                )
                );

        return http.build();
    }

//    https://github.com/codingspecialist/Springboot-Security-OAuth2.0-V2/blob/master/src/main/java/com/cos/securityex01/config/oauth/provider/NaverUserInfo.java

} // end class


