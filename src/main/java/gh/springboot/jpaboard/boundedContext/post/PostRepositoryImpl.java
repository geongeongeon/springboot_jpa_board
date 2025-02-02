package gh.springboot.jpaboard.boundedContext.post;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static gh.springboot.jpaboard.boundedContext.post.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> searchPostsByTitleOrContent(String kw, Pageable pageable) {
        QueryResults<Post> searchedResult = jpaQueryFactory.selectFrom(post)
                .where(post.title.contains(kw)
                        .or(post.content.contains(kw)))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(searchedResult.getResults(), pageable, searchedResult.getTotal());
    }

}