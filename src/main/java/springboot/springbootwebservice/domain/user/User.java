package springboot.springbootwebservice.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.springbootwebservice.domain.BaseTimeEntity;

@Getter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    // JPA로 데이터베이스로 지정할 때 Enum 값을 어떤 형태로 저장할지 결정.
    // 기본적으로는 int로 된 숫자가 저장. 숫자로 저장되면 데이터베이스로 확인할 때 그 값이 무슨 고드를 의미하는지 알 수가 없다.
    // 그래서 문자열(EnumType.STRING)로 저장될 수 있도록 선언
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
