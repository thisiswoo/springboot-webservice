package springboot.springbootwebservice.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter // 선언된 모든 필드의 get메서드 생성 즉, getName(), getAmount() 생성
@RequiredArgsConstructor // 선언된 모든 final 필드가 포함된 생성자를 생성, final이 없는 필드의 생성자에 포함되지 않는다.
public class HelloResponseDTO {
    private final String name;
    private final int amount;

//    // 위 @Getter
//    public String getName() {
//        return name;
//    }
//
//    public int getAmount() {
//        return amount;
//    }
//
//    // 위 @RequiredArgsConstructor
//    public HelloResponseDTO(String name, int amount) {
//        this.name = name;
//        this.amount = amount;
//    }
}
