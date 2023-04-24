package springboot.springbootwebservice.config.auth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import springboot.springbootwebservice.config.auth.dto.OAuthAttributes;
import springboot.springbootwebservice.config.auth.dto.SessionUser;
import springboot.springbootwebservice.domain.user.User;
import springboot.springbootwebservice.domain.user.UserRepository;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // RegistrationId()
        // 현재 로그인 진행 중인 서비스를 구분하는 코드
        // 현재 구글만 사용하는 불필요한 값이지만, 이후 네이버 로그인 연동시에 네이버 로그인 인지, 구글 로그인인지 구분하기 위함
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // UserNameAttributeName()
        // OAuth2 로그인 진행 시 키가 되는 필드값을 이야기 한다. Primary Key와 같은 의미.
        // 구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카 등은 기본 지원하지 않는다. 구굴의 기본 코드는 "sub"
        // 이후 네이버 로그인과 구글 로그인을 동시에 지원할 때 사용
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuthAttributes
        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담은 클래스.
        // 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용
        OAuthAttributes authAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(authAttributes);

        // SessionUser
        // 세션에 사용자 정보를 저장하니 위한 DTO 클래스
        // 왜 User 클래스를 쓰지 않고 새로 만들어서 쓰는지?
        // -> 만약 User 클래스를 그대로 사용했으면 다음과 같은 에러가 발생한다.
        // Failed to convert from type [java.lang.Object]
        // 이는 세션에 저장하기 위해 User 클래스를 세션에 저장하려고 하니, User 클래스에 직렬화를 구현하지 않았다.는 의미의 에러이다.
        httpSession.setAttribute("user", new SessionUser(user));
//        httpSession.setAttribute("user", user);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                authAttributes.getAttributes(),
                authAttributes.getNameAttributeKey());

    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
