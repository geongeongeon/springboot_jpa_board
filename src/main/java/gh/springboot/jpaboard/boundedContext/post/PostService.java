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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public Post writePost(SiteUser user, String title, String content) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .author(user)
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .answerCount(0)
                .build();

        user.addPost(post);

        return postRepository.save(post);
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

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public void modifyPost(Post post, String title, String content) {
        post.setTitle(title);
        post.setContent(content);
        post.setModifyDate(LocalDateTime.now());

        postRepository.save(post);
    }

    public Long getAnswerCountByPostId(Long id) {
        return postRepository.searchAnswerCountByPostId(id);
    }

    public boolean likePost(Post post, SiteUser loginUser) {
        if (post.getLikedUsers().contains(loginUser)) {
            return false;
        } else {
            post.setLikeCount(post.getLikeCount() + 1);
            post.addLikedUser(loginUser);

            postRepository.save(post);

            return true;
        }
    }

    public boolean dislikePost(Post post, SiteUser loginUser) {
        if (post.getDislikedUsers().contains(loginUser)) {
            return false;
        } else {
            post.setDislikeCount(post.getDislikeCount() + 1);
            post.addDislikedUser(loginUser);

            postRepository.save(post);

            return true;
        }
    }

    public List<Post> getPostsByAuthorId(Long id) {
        return postRepository.getPostsByAuthorId(id);
    }

    @Transactional
    public void removeAllLikesAndDislikes(SiteUser user) {
        for (Post post : new HashSet<>(user.getLikePosts())) {
            post.removeLikedUser(user);
        }

        for (Post post : user.getDislikePosts()) {
            post.removeDislikedUser(user);
        }
    }

    public void removeAllLikesAndDislikesFromPost(Post post) {
        for (SiteUser user : new HashSet<>(post.getLikedUsers())) {
            post.removeLikedUser(user);
        }

        for (SiteUser user : new HashSet<>(post.getDislikedUsers())) {
            post.removeDislikedUser(user);
        }
    }

}