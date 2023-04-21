package springboot.springbootwebservice.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// JpaRepository<Entity 클래스, PK 타입>를 상속하면 기본적인 CRUD 메서드가 자동으로 생성
// @Repository를 추가할 필요도 없다.
// 주의!
// Entity 클래스와 기본 Entity Repository는 함께 위치해야 하는 점.
// 둘은 아주 밀접한 관계이고, Entity 클래스는 기본 Repository 없이는 제대로 역할을 할 수 가 없다.
// 나중에 프로젝트 규모가 커져 도메인별로 프로젝트를 분리해야 한다면 이때 Entity 클래스와 기본 Repository는 함께 움직여야 하므로 도메인 패키지에 함께 관리하게 된다.
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
