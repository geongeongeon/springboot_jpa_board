package gh.springboot.jpaboard.boundedContext.post;

import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
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

        user.addPost(post);

        postRepository.save(post);
    }

    public Page<Post> getPostList(int page, String kw) {
        List<Sort.Order> sortIdDesc = new ArrayList<>();
        sortIdDesc.add(Sort.Order.desc("id"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortIdDesc));

        if (kw == null || kw.trim().isEmpty()) {
            return postRepository.findAll(pageable);
        }

        return postRepository.searchPostsByTitleOrContentOrAuthor(kw, pageable);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public void increaseHitCount(Post post) {
        long hitCount = post.getHitCount();
        post.setHitCount(hitCount + 1);

        postRepository.save(post);
    }

    public void updateAuthorToNull(Post post) {
        post.setAuthor(null);

        postRepository.save(post);
    }

}