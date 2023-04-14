package springboot.springbootwebservice.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springboot.springbootwebservice.domain.posts.Posts;
import springboot.springbootwebservice.domain.posts.PostsRepository;
import springboot.springbootwebservice.web.dto.PostsSaveRequestDTO;
import springboot.springbootwebservice.web.dto.PostsUpdateRequestDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiContollerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @DisplayName("Posts 등록")
    public void Posts_등록() throws Exception {
        // given
        String title = "title";
        String content = "content";
        PostsSaveRequestDTO requestDTO = PostsSaveRequestDTO.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDTO, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("Posts 수정")
    public void Posts_수정() throws Exception {
        // given
        Posts savedPosts = postsRepository.save(Posts.builder()
                        .title("title")
                        .content("content")
                        .author("author")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "contents";

        PostsUpdateRequestDTO requestDTO = PostsUpdateRequestDTO.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDTO> requestEntity = new HttpEntity<>(requestDTO);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);


        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        // responseEntity의 출력 결과
        // <
        // 200 OK OK,
        // 1,
        // [Content-Type:"application/json",
        // Transfer-Encoding:"chunked",
        // Date:"Fri, 14 Apr 2023 00:36:36 GMT",
        // Keep-Alive:"timeout=60",
        // Connection:"keep-alive"]
        // >

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle());
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}