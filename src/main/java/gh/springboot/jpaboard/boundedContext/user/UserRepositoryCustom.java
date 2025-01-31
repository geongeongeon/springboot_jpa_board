package gh.springboot.jpaboard.boundedContext.user;

import java.util.List;

public interface UserRepositoryCustom {

    List<SiteUser> searchUsersByUsernameOrEmail(String kw);

}