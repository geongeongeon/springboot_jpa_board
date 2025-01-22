package gh.springboot.jpaboard.boundedContext.base;

import gh.springboot.jpaboard.boundedContext.user.UserRole;
import gh.springboot.jpaboard.boundedContext.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevInitData {

    @Bean
    CommandLineRunner initData(UserService userService) {
        return args -> {
            userService.createUser("admin", "1234", "admin@test.com", UserRole.ADMIN);

            userService.createUser("start_user1", "1234", "start_user1@test.com", UserRole.USER);
            userService.createUser("start_user2", "1234", "start_user2@test.com", UserRole.USER);
        };
    }

}