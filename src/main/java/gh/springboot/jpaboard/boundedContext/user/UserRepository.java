package gh.springboot.jpaboard.boundedContext.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE site_user AUTO_INCREMENT = 1;", nativeQuery = true)
    void clearIdAutoIncrement();

    SiteUser findByUsername(String user1);

}