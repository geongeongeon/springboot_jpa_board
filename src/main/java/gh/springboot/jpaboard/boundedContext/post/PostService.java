package gh.springboot.jpaboard.boundedContext.post;

import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void writePost(SiteUser user, String title, String content) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .author(user)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }

}