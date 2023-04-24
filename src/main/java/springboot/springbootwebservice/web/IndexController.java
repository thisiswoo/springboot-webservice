package springboot.springbootwebservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import springboot.springbootwebservice.config.auth.LoginUser;
import springboot.springbootwebservice.config.auth.dto.SessionUser;
import springboot.springbootwebservice.service.PostsService;
import springboot.springbootwebservice.web.dto.PostsResponseDTO;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    // Model : 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있다. postsService.findAllDesc()에서 가져온 결과를 "posts"로 index.html에 전달.
    // @LoginUser SessionUser user : 기존 (User) httpSession.getAttribute("user")로 가져오던 세션 정보 값이 개선. 이제 어느 Contoller든지 @LoginUser만 사용하면 세션 정보를 가져올 수 있게 된다.
    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());
        if (user != null) model.addAttribute("userName", user.getName());
        return "index";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDTO dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }


}
