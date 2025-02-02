package gh.springboot.jpaboard.boundedContext.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> searchPostsByTitleOrContent(String kw, Pageable pageable);

}