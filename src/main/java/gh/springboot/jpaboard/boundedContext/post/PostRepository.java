package gh.springboot.jpaboard.boundedContext.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE post AUTO_INCREMENT = 0;", nativeQuery = true)
    void clearIdAutoIncrement();

    List<Post> getPostsByAuthorId(Long id);

}