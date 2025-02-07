package gh.springboot.jpaboard.boundedContext.answer;

import gh.springboot.jpaboard.boundedContext.post.Post;
import gh.springboot.jpaboard.boundedContext.post.PostService;
import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post/{id}/answer")
public class AnswerController {

    private final UserService userService;

    private final PostService postService;

    private final AnswerService answerService;

    @PostMapping("/write")
    public String writeAnswer(@PathVariable("id") Long id, AnswerDto answerDto, Principal principal) {
        Optional<SiteUser> optUser = userService.getSiteUserByUsername(principal.getName());
        SiteUser user = optUser.isPresent() ? optUser.get() : null;

        Optional<Post> optPost = postService.getPostById(id);
        Post post = optPost.isPresent() ? optPost.get() : null;

        Answer answer = answerService.writeAnswer(user, post, answerDto.getContent());

        return "redirect:/post/detail/%s#answer_%d".formatted(id, answer.getId());
    }

    @PostMapping("/delete/{answer_id}")
    public String deleteAnswer(@PathVariable("id") Long id, @PathVariable("answer_id") Long answer_id, Principal principal) {
        Optional<Answer> optAnswer = answerService.getAnswerById(answer_id);
        Answer answer = optAnswer.isPresent() ? optAnswer.get() : null;

        if (answer.getAuthor().getUsername().equals(principal.getName())) {
            answerService.deleteAnswer(answer_id);
        }

        return "redirect:/post/detail/%s".formatted(id);
    }

}