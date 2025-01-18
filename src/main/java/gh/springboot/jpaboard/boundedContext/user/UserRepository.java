package gh.springboot.jpaboard.boundedContext.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

    SiteUser findByUsername(String user1);

}