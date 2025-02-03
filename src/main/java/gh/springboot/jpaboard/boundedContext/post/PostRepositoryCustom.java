package gh.springboot.jpaboard.boundedContext.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> searchPostsByTitleOrContentOrAuthor(String kw, Pageable pageable);

}