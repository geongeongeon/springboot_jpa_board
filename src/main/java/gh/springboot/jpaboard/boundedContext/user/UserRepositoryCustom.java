package gh.springboot.jpaboard.boundedContext.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {

    List<SiteUser> searchUsersByUsernameOrEmail(String kw);

    Page<SiteUser> searchUsersByUsernameOrEmail(String kw, Pageable pageable);

}