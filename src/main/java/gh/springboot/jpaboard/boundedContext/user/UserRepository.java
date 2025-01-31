package gh.springboot.jpaboard.boundedContext.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long>, UserRepositoryCustom {

    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE site_user AUTO_INCREMENT = 1", nativeQuery = true)
    void clearIdAutoIncrement();

    Optional<SiteUser> findByUsername(String username);

    Optional<SiteUser> findByEmail(String email);

}