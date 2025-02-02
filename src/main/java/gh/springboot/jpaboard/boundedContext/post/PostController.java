package gh.springboot.jpaboard.boundedContext.post;

import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final UserService userService;

    private final PostService postService;

    @GetMapping("/list")
    public String showPostList(Model model, @RequestParam(defaultValue = "0") int page, String kw) {
        Page<Post> pagedPosts = postService.getPostList(page, kw);
        model.addAttribute("pagedPosts", pagedPosts);

        return "post/list";
    }

    @GetMapping("/write")
    public String showWritePostForm(PostDto postDto) {
        return "post/write";
    }

    @PostMapping("/write")
    public String writePost(@Valid PostDto postDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "post/write";
        }

        if (postDto.getTitle().length() > 50) {
            bindingResult.rejectValue("title", "titleLengthError", "제목을 50글자 이하로 입력해주세요.");

            return "post/write";
        }

        Optional<SiteUser> writeUser = userService.getSiteUserByUsername(principal.getName());

        writeUser.ifPresent(user -> {
            postService.writePost(user, postDto.getTitle(), postDto.getContent());
        });

        return "redirect:/post/list";
    }

}