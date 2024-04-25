package com.example.crud_project.config.oauth;

import com.example.crud_project.config.auth.PrincipalDetails;
import com.example.crud_project.config.oauth.provider.GoogleUserInfo;
import com.example.crud_project.config.oauth.provider.NaverUserInfo;
import com.example.crud_project.config.oauth.provider.OAuth2UserInfo;
import com.example.crud_project.dao.UserDAO;
import com.example.crud_project.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserDAO userDAO;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리 되는 함수.
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    // 리소스 서버(소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행 하고자 하는 기능을 명시할 수 있다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("PrincipalOauth2UserService 입니다.");
        System.out.println("getClientRegistration : "+ userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인가능
        System.out.println("getAccessToken : "+ userRequest.getAccessToken().getTokenValue());
//        System.out.println("=======================");

        OAuth2User oauth2User= super.loadUser(userRequest); // google 의 회원 프로필 조회

        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인을 완료 -> code 를 리턴(Oauth-Client 라이브러리가 받는다.) -> AccessToken 요청
        // 여기까지가 userRequest 정보 -> 회원프로필을 받아야함(loudUser 함수 호출) -> 구글로부터 회원 프로필을 받아준다.
        System.out.println("getAttributes : "+ oauth2User.getAttributes()); //이 정보를 바탕으로 회원가입 강제 진행

//        // 강제 회원가입 진행
        OAuth2UserInfo oAuth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println(" 구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());

        }else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println(" 네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));

        }else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
          //  String username = "kakao_" + oauth2User.getAttributes().get("id");

            System.out.println(" kakao 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("kakao_account")); // kakao_account?
        }
        else {
            System.out.println("해당 코드는 구글만 지원합니다.");
        }

        //assert oAuth2UserInfo != null;
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider+ "_"+ providerId; // google_101207104389948200344
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

//        String provider = userRequest.getClientRegistration().getRegistrationId();
//        String providerId = oauth2User.getAttribute("sub");
//        String username = provider+ "_"+ providerId; // google_101207104389948200344
//        String password = bCryptPasswordEncoder.encode("겟인데어");
//        String email = oauth2User.getAttribute("email");
//        String role = "ROLE_USER";

        // 회원가입시 같은 유저가 있는지 검사.
        User user = userDAO.findByUsername(username);
        if (user == null){
            System.out.println("Oauth2User Service if 부분 ===OAuth 로그인이 최초 입니다. ===");
            user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userDAO.save(user);
        }else {
            System.out.println("===로그인 한적이 있다요. 자동 회원가입이 되어 있다요. ===");
        }

        return new PrincipalDetails(user, oauth2User.getAttributes());
    }

}
