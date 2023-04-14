package springboot.springbootwebservice.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
}