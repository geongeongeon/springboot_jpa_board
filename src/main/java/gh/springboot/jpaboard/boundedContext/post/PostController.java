package gh.springboot.jpaboard.boundedContext.post;

import gh.springboot.jpaboard.boundedContext.answer.Answer;
import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

        if (postDto.getTitle().length() > 100) {
            bindingResult.rejectValue("title", "titleLengthError", "제목을 100글자 이하로 입력해주세요.");

            return "post/write";
        }

        Optional<SiteUser> optWriteUser = userService.getSiteUserByUsername(principal.getName());
        SiteUser writeUser = optWriteUser.isPresent() ? optWriteUser.get() : null;

        Post post = postService.writePost(writeUser, postDto.getTitle(), postDto.getContent());

        return "redirect:/post/detail/%s".formatted(post.getId());
    }

    @GetMapping("/detail/{id}")
    public String showPostDetail(Model model, @PathVariable("id") Long id, Principal principal) {
        Optional<Post> optPost = postService.getPostById(id);

        optPost.ifPresent(post -> {
            if (post.getAuthor() == null || !principal.getName().equals(post.getAuthor().getUsername())) {
                postService.increaseHitCount(post);
            }

            model.addAttribute("post", post);
            model.addAttribute("loginUser", principal.getName());
        });

        return "post/detail";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Principal principal) {
        Optional<Post> optPost = postService.getPostById(id);
        String postAuthorUsername = optPost.get().getAuthor().getUsername();

        if (postAuthorUsername.equals(principal.getName())) {
            postService.deletePost(id);

            return "redirect:/post/list";
        }

        return "redirect:/post/list/%s".formatted(id);
    }

    @GetMapping("/modify/{id}")
    public String showModifyPostForm(@PathVariable("id") Long id, PostDto postDto) {
        Optional<Post> optPost = postService.getPostById(id);

        optPost.ifPresent(post -> {
            postDto.setTitle(post.getTitle());
            postDto.setContent(post.getContent());
        });

        return "post/write";
    }

    @PostMapping("/modify/{id}")
    public String modifyPost(@PathVariable("id") Long id, @Valid PostDto postDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post/write";
        }

        if (postDto.getTitle().length() > 100) {
            bindingResult.rejectValue("title", "titleLengthError", "제목을 100글자 이하로 입력해주세요.");

            return "post/write";
        }

        Optional<Post> optPost = postService.getPostById(id);

        optPost.ifPresent(post -> {
            postService.modifyPost(post, postDto.getTitle(), postDto.getContent());
        });

        return "redirect:/post/detail/%s".formatted(id);
    }

}