package springboot.springbootwebservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springboot.springbootwebservice.service.PostsService;
import springboot.springbootwebservice.web.dto.PostsResponseDTO;
import springboot.springbootwebservice.web.dto.PostsSaveRequestDTO;

@RequiredArgsConstructor
@RestController
public class PostsApiContoller {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDTO requestDTO) {
        return postsService.save(requestDTO);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsSaveRequestDTO requestDTO) {
        return postsService.update(id, requestDTO);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDTO findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }
}
