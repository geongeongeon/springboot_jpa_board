package gh.springboot.jpaboard.boundedContext.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

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

}