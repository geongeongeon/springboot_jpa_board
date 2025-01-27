package gh.springboot.jpaboard.boundedContext.base;

import gh.springboot.jpaboard.boundedContext.user.UserRepository;
import gh.springboot.jpaboard.boundedContext.user.UserRole;
import gh.springboot.jpaboard.boundedContext.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

@Configuration
@Profile("dev & !test")
public class DevInitData {

    @Bean
    @Order(1)
    CommandLineRunner removeAllPreviousUserData(@Qualifier("userRepository") UserRepository userRepository) {
        return args -> {
            userRepository.deleteAll();
            userRepository.clearIdAutoIncrement();
        };
    }

    @Bean
    @Order(2)
    CommandLineRunner createStartUserData(UserService userService) {
        return args -> {
            userService.createUser("admin", "1234", "admin@test.com", UserRole.ADMIN);

            userService.createUser("start_user1", "1234", "start_user1@test.com", UserRole.USER);
            userService.createUser("start_user2", "1234", "start_user2@test.com", UserRole.USER);
        };
    }

}
