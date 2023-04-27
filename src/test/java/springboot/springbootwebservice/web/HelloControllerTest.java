package springboot.springbootwebservice.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;

// JUnit5로 넘어오면서 @RunWith(SrpingRunner.class)에서 해당 어노테이션으로 바뀌었다.
// 또한 @SpringBootTest를 적용하면 @ExtendWith(SpringExtension.class)를 포함하고 있어 생략이 가능하다.
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(SpringExtension.class) // JUnit5로 넘어오면서 @RunWith(SrpingRunner.class) 대체
@WebMvcTest(controllers = HelloController.class) // Web(Spring MVC)에 집중할 수 있는 어노테이션이다. 선언할 경우 @Controller, @ControllerAdvice등을 사용할 수 있다. 단, @Service, @Component, @Respository등은 사용할 수 없다.
public class HelloControllerTest {

    @Autowired // 스프링 빈(Bean)을 주입
    private MockMvc mvc; // 개발한 웹 프로그램을 실제 서버에 배포하지 않고도 테스트를 위한 요청을 제공 즉, 웹 API 테스트 할 때 사용, 이 클래스를 통해 HTTP GET, POST등에 대한 API테스트

    @Test // test할 메서드
    @DisplayName("hello가 리턴된다")
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";
        // 변경 후
        mvc.perform(get("/hello").with(oauth2Login()))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));

        /* 변경 전
        mvc.perform(get("/hello"))  // MockMvc를 통해 /hello 주소로 HTTP GET 요청, 체이닝이 지원되어 아래와 같이 여러 검증 기능을 이어서 선언
                .andExpect(status().isOk()) // mvc.perform의 결과를 검증, HTTP Header의 Status를 검증, 우리가 흔히 알고 있는 200, 404, 500등의 상태를 검증
                .andExpect(content().string(hello)); // mvc.perform의 결과를 검증, 응답 본문의 내용을 검증, Controller에서 "hello"를 리턴하기 때문에 이 값이 맞는지 검증
        */
    }

    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;
        // 변경 후
        mvc.perform(get("/hello/dto").with(oauth2Login()).param("name", name).param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));

        /* 변경 전
        mvc.perform(
                get("/hello/dto") // HelloController의 GetMapping("/hello/dto")의 메핑 테스트 url 설정
                        // param() : API 테스트할 때 사용될 요청 파라미터를 설정, 단, 값은 String만 허용, 그래서 숫자/날짜 등의 데이터도 등록할 때는 문자열로 변경해야만 가능
                        .param("name", name)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                // jsonPath() : JSON 응답값을 필드별로 검증할 수 있는 메서드, $을 기준으로 필드명을 명시
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
        */
    }

}
