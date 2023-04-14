package springboot.springbootwebservice.web.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat; // assertj라는 테스트 검증 라이브러리의 검증 메서드,

public class HelloResponseDTOTest {

    @Test
    @DisplayName("롬복_기능_테스트")
    public void 롬복_기능_테스트() {
        // given
        String name = "test";
        int amount = 1000;

        // when
        HelloResponseDTO dto = new HelloResponseDTO(name, amount);

        // then
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);

        // 설명
        // assertThat()
        //  - assertj 라는 테스트 검증 라이브러리의 검증 메서드
        //  - 검증하고 싶은 대상을 메서드 인자로 받는다.
        //  - 메서드 체이닝이 지원되어 isEqualTo()와 같은 메서드를 이어서 사용 가능.

        // isEqualTo()
        //  - assertj의 동등 비교 메서드
        //  - assertThat()에 있는 값과 isEqualTo()의 값을 비교해서 같을 때만 성공

        // 여기서 잠깐! JUnit의 assert() 메서드와 assertj의 assert() 메서드의 차이점
        // JUnit    - assertEquals(a, b);
        // assertj  - assertThat(a).isEqualTo(b);

        // assertj의 가독성이 조금더 나아보니며, 메서드 체이닝을 지원.
    }
}
