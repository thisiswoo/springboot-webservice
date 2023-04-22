package springboot.springbootwebservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.springbootwebservice.domain.posts.Posts;
import springboot.springbootwebservice.domain.posts.PostsRepository;
import springboot.springbootwebservice.web.dto.PostsListResponseDTO;
import springboot.springbootwebservice.web.dto.PostsResponseDTO;
import springboot.springbootwebservice.web.dto.PostsSaveRequestDTO;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor    // final이 선언된 모든 필드를 인자값으로 하는 생성자를 대신 생성해주는 어노테이션
@Service
public class PostsService {

    // 스프링에서 Bean을 주입 받는 방법은 @Autowired, @Setter, 생성자 이다.
    // 이들 중 가장 권장하는 방법이 생성자 주입 방식이다.
    // @Autowired 권장하지 않는다.
    // 생성자로 Bean을 주입 받으면 @Autowired와 동일한 효과를 볼 수 있다.
    // final이 선언된 모든 필드를 인자값으로 하는 생성자를 롬복의 @RequiredArgsConstructor가 대신 생성해준다
    private final PostsRepository postsRepository;

    // @Transactional이 붙은 메서드는 메서드가 포함하고 있는 작업 중에 하나라도 실패할 경우 전체 작업을 취소한다.
    // Test환경에서의 (TestTransaction.제공메서드)는 메서드가 종료될 때 자동으로 RollBack 된다.
    @Transactional
    public Long save(PostsSaveRequestDTO requestDTO) {
        return postsRepository.save(requestDTO.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsSaveRequestDTO requestDTO) {

        // IllegalArgumentException이란?
        // 적합하지 않거나(Illegal), 적절하지 못한 인자(매개 값)을 메서드에 넘겨주었을 때 발생
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        posts.update(requestDTO.getTitle(), requestDTO.getContent());
        return id;
    }

    public PostsResponseDTO findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new PostsResponseDTO(entity);
    }

    @Transactional(readOnly = true) // 트랜잭션 범위 유지하되, 조회 기능만 남겨두어 조회 속도가 개선되기 때문에 등록, 수정, 삭제 기능이 전혀 없는 서비스 메서드에 사용하는 것을 추천
    public List<PostsListResponseDTO> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDTO::new) // 해당 코드는 .map(posts -> new PostsListResponseDTO(posts)) 와 같다.
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        // 존재하는 Posts인지 확인을 위해 엔티티 조회 후 그대로 삭제.
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이없습니다. id=" + id));
        postsRepository.delete(posts);  // 엔티티를 파라미터로 삭제할 수도 있고, deleteById 메서드를 이용하면 id로 삭제할수도 있다.
    }
}
