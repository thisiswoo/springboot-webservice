package springboot.springbootwebservice.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)  // 이 어노테이션이 생성될 수 있는 위치를 지정. PARAMETER로 지정했으니 메서드의 파라미터로 선언도니 객체에서만 사용할 수 있다. 이외에도 클래스 선언문에 쓸 수 있는 TYPE 등이 있다.
@Retention(RetentionPolicy.RUNTIME) // 이 파일을 어노테이션 클래스로 지정. LoginUser 라는 이름을 가진 어노테이션이 생성되었다고 보면 된다.
public @interface LoginUser {
}
