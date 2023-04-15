package springboot.springbootwebservice.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach // JUnit5 <-> @After JUnit4
    public void cleanUp() {
        postsRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 저장 불러오기")
    public void 게시글_저장_불러오기() throws Exception {
        // given
        String title = "테스트 게시물";
        String content = "테스트 본문";

        postsRepository.save(
                Posts.builder()
                        .title(title)
                        .content(content)
                        .author("thisiswoo0594@gmail.com")
                .build());
        // when
        List<Posts> list = postsRepository.findAll();

        // then
        Posts post = list.get(0);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("BaseEntity 등록")
    public void BaseEntity_등록() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 4, 15, 0, 0, 0);
        postsRepository.save(
                Posts.builder()
                        .title("title")
                        .content("content")
                        .author("author")
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>>>>> createDate=" + posts.getCreatedDate() + ", modifyDate=" + posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);

        // 출력
        // >>>>>>>>>>>> createDate=2023-04-15T13:24:03.496347, modifyDate=2023-04-15T13:24:03.496347
        // BaseTimeEntity를 통해 JAP Auditing 기능을 설정해주어 create, modify date를 개발자가 일일이 지정해 줄 필요 없게 되었다.
        // JAP Auditing가 자동으로 create, modify date를 자동 생성해주게 된다.성
    }
}