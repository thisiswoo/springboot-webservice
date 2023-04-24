package springboot.springbootwebservice.config.auth;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import springboot.springbootwebservice.domain.user.Role;

@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security를 활성화
@Configuration
public class SpringSecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    // spring security 적용 하지 않을 URL 리스트
    private static final String[] AUTH_WHITELIST = {
            "/",
            "/css/**",
            "/image/**",
            "/js/**",
            "/h2-console/**"
    };
    // 인증 필요 리스트
    private static final String[] VERIFICATION_AUTH_LIST = {
            "/api/v1/**"
    };

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // h2-console 화면을 사용하기 위해 해당 옵션들을 disable 처리
                .and()
                .authorizeHttpRequests( // antMatchers(), mvcMatchers(), regexMatchers()가 -> authorizeHttpRequests() 또는 securityMatchers()로 변경
                        request -> request
                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                                .requestMatchers(AUTH_WHITELIST).permitAll()
//                                .requestMatchers("/", "/css/**", "/image/**", "/js/**", "/h2-console/**").permitAll()
                                .requestMatchers(VERIFICATION_AUTH_LIST).hasRole(Role.USER.name())    // 권한 관리 대상을 지정하는 옵션, URL, HTTP 메서드별로 관리가 가능, "/" 등 지정된 URL들을 permitAll() 옵션을 통해 전체 열람 권한을 줌, "/api/v1/**"주소를 가진 API는 USER 권한을 가진 사람만 가능
                                .anyRequest().authenticated()   // 설정된 값들 이외 나머지 URL들을 나타낸다. 여기선 authenticated()을 추가하여 나머지 URL들은 모두 인증된 사용자들에게만 허용. 즉, 인증된 사용자, 로그인한 사용자들만 허용
                )
                .logout().logoutSuccessUrl("/")
                .and()
                .oauth2Login()  // OAuth2.0 로그인 기능에 대한 여러 설정의 진입점
                    .userInfoEndpoint() // OAuth2.0 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                    .userService(customOAuth2UserService);  // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록. 리소스 서버(즉, 소설 서비스)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시
        return http.build();
    }
}
