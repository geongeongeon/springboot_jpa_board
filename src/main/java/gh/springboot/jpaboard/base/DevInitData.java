package gh.springboot.jpaboard.base;

import gh.springboot.jpaboard.boundedContext.post.PostRepository;
import gh.springboot.jpaboard.boundedContext.post.PostService;
import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserRepository;
import gh.springboot.jpaboard.boundedContext.user.UserRole;
import gh.springboot.jpaboard.boundedContext.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.util.Optional;

@Configuration
@Profile("dev & !test")
public class DevInitData {

    @Bean
    @Order(1)
    CommandLineRunner removeAllPreviousPostData(PostRepository postRepository) {
        return args -> {
            postRepository.deleteAll();
            postRepository.clearIdAutoIncrement();
        };
    }

    @Bean
    @Order(2)
    CommandLineRunner removeAllPreviousUserData(UserRepository userRepository) {
        return args -> {
            userRepository.deleteAll();
            userRepository.clearIdAutoIncrement();
        };
    }

    @Bean
    @Order(3)
    CommandLineRunner createStartUserData(UserService userService) {
        return args -> {
            userService.createUser("admin", "1234", "admin@test.com", UserRole.ADMIN);

            userService.createUser("start_user1", "1234", "start_user1@test.com", UserRole.USER);
            userService.createUser("start_user2", "1234", "start_user2@test.com", UserRole.USER);
        };
    }

    @Bean
    @Order(4)
    CommandLineRunner createStartPostData(UserService userService, PostService postService) {
        return args -> {
            Optional<SiteUser> admin = userService.getSiteUserByUsername("admin");
            admin.ifPresent(user -> postService.writePost(user,"게시글 제목 1", "게시글 내용 1"));

            Optional<SiteUser> start_user1 = userService.getSiteUserByUsername("start_user1");
            start_user1.ifPresent(user -> postService.writePost(user,"게시글 제목 2", "게시글 내용 2"));

            Optional<SiteUser> start_user2 = userService.getSiteUserByUsername("start_user2");
            start_user2.ifPresent(user -> postService.writePost(user,"게시글 제목 3", "게시글 내용 3"));
        };
    }

}