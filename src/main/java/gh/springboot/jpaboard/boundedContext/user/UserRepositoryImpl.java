package gh.springboot.jpaboard.boundedContext.user;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static gh.springboot.jpaboard.boundedContext.user.QSiteUser.siteUser;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SiteUser> searchUsersByUsernameOrEmail(String kw) {
        return jpaQueryFactory
                .selectFrom(siteUser)
                .where(siteUser.username.contains(kw)
                        .or(siteUser.email.contains(kw)))
                .fetch();
    }

    @Override
    public Page<SiteUser> searchUsersByUsernameOrEmail(String kw, Pageable pageable) {
        QueryResults<SiteUser> searchedResult = jpaQueryFactory
                .selectFrom(siteUser)
                .where(siteUser.username.contains(kw)
                        .or(siteUser.email.contains(kw)))
                .orderBy(siteUser.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(searchedResult.getResults(), pageable, searchedResult.getTotal());
    }

}