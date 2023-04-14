package springboot.springbootwebservice.web.dto;

import lombok.Getter;
import springboot.springbootwebservice.domain.posts.Posts;

@Getter
public class PostsResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDTO(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
