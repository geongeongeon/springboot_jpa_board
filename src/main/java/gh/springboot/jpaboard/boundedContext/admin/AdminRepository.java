package gh.springboot.jpaboard.boundedContext.admin;

import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends UserRepository {

    @Override
    void clearIdAutoIncrement();

    @Override
    Optional<SiteUser> findByUsername(String username);

    @Override
    Optional<SiteUser> findByEmail(String email);

    Page<SiteUser> findAll(Pageable pageable);

}