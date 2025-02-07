package gh.springboot.jpaboard.boundedContext.post;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static gh.springboot.jpaboard.boundedContext.post.QPost.post;
import static gh.springboot.jpaboard.boundedContext.answer.QAnswer.answer;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> searchPostsByTitleOrContentOrAuthor(String kw, Pageable pageable) {
        QueryResults<Post> searchedResult = jpaQueryFactory.selectFrom(post)
                .where(post.title.contains(kw)
                        .or(post.content.contains(kw)
                                .or(post.author.username.contains(kw))))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(searchedResult.getResults(), pageable, searchedResult.getTotal());
    }

    @Override
    public Long searchAnswerCountByPostId(Long id) {
        return jpaQueryFactory
                .select(answer.count())
                .from(answer)
                .where(post.id.eq(id))
                .fetchOne();
    }

}