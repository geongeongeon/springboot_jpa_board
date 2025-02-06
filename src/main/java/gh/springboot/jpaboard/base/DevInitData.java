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
            admin.ifPresent(user -> postService.writePost(user,
                    "Spring Boot JPA 게시판 기초",
                    "<p><strong>Spring Boot와 JPA를 사용하여 게시판을 구현하는 방법을 다루겠습니다.</strong> 이 게시판 시스템은 사용자가 제목과 내용을 입력하고 이를 데이터베이스에 저장할 수 있는 기능을 제공합니다. Spring Boot 프로젝트를 생성하고 필요한 의존성을 추가한 후, <span style=\"color: blue;\">application.properties</span> 파일에서 데이터베이스 연결 정보를 설정합니다. 이후, <code>Post</code>라는 엔티티 클래스를 작성하여 게시글 정보를 담습니다. <em>PostRepository</em> 인터페이스를 작성하여 데이터베이스와의 CRUD 연동을 처리하고, <strong>PostController</strong>에서 HTTP 요청을 처리합니다. 이렇게 기본적인 CRUD 기능을 갖춘 게시판을 구현할 수 있습니다. Spring Boot와 JPA의 기본적인 설정과 사용법을 익히는 데 유용한 예제입니다.</p>\n" +
                            "<p>게시글 작성, 수정, 삭제 등의 기본적인 기능을 구현하는 방법을 이해할 수 있습니다. 이 시스템을 기반으로 댓글 기능, 좋아요 기능 등의 추가적인 기능을 더할 수 있으며, 점차적으로 확장할 수 있는 구조를 갖추게 됩니다. 이러한 시스템은 실제 웹 애플리케이션에서 기본적으로 사용되는 기능들을 포함하고 있어 매우 실용적입니다.</p>"));

            Optional<SiteUser> start_user1 = userService.getSiteUserByUsername("start_user1");
            start_user1.ifPresent(user -> postService.writePost(user,
                    "Spring Boot에서 REST API와 JPA 사용하기",
                    "<p><strong>Spring Boot를 사용하여 RESTful API를 구축하고, JPA를 통해 데이터베이스와 연동하는 방법을 설명하겠습니다.</strong> Spring Boot 프로젝트를 설정할 때 <code>spring-boot-starter-web</code>과 <code>spring-boot-starter-data-jpa</code> 의존성을 추가해야 합니다. 이 의존성들을 통해 Spring Boot 애플리케이션에서 웹 애플리케이션과 데이터베이스 연동이 가능해집니다. 또한, <span style=\"color: green;\">application.properties</span> 파일에 데이터베이스 연결 정보를 설정하여 데이터베이스와의 연결을 맞추고, <strong>Post</strong> 엔티티 클래스를 정의하여 게시글 정보를 데이터베이스에 저장할 수 있습니다.</p>\n" +
                            "<p>JPA를 사용할 때는 <code>@Entity</code>, <code>@Id</code> 어노테이션을 통해 <strong>Post</strong> 클래스가 데이터베이스 테이블과 매핑됩니다. <strong>PostController</strong> 클래스에서는 HTTP 요청을 처리하고, 각 요청에 맞는 메서드를 실행하여 데이터를 처리합니다. <code>@RestController</code> 어노테이션을 사용하면 API 요청을 처리할 수 있으며, <code>@GetMapping</code>, <code>@PostMapping</code>, <code>@PutMapping</code>, <code>@DeleteMapping</code>을 사용하여 GET, POST, PUT, DELETE 요청을 처리합니다. 이를 통해 간단한 게시판 REST API를 만들 수 있습니다.</p>\n" +
                            "<p>Spring Boot와 JPA를 결합하면 데이터를 효율적으로 처리할 수 있으며, RESTful API 설계를 통해 다양한 클라이언트와의 연동이 가능해집니다. 이 방식은 웹 애플리케이션뿐만 아니라 모바일 애플리케이션과도 쉽게 연동할 수 있습니다. 이러한 구조는 확장성 있는 시스템을 구축하는 데 유리합니다.</p>"));

            Optional<SiteUser> start_user2 = userService.getSiteUserByUsername("start_user2");
            start_user2.ifPresent(user -> postService.writePost(user,
                    "JPA와 Spring Boot에서의 트랜잭션 처리",
                    "<p><strong>Spring Boot와 JPA에서 트랜잭션을 처리하는 방법에 대해 알아보겠습니다.</strong> 트랜잭션은 데이터베이스의 여러 작업을 하나의 단위로 묶어 처리하는 중요한 개념입니다. 트랜잭션이 올바르게 처리되지 않으면 데이터의 일관성이 깨질 수 있기 때문에, 데이터베이스 작업을 진행할 때 반드시 트랜잭션을 사용해야 합니다. Spring Boot에서는 <code>@Transactional</code> 어노테이션을 사용하여 트랜잭션을 처리할 수 있습니다. 이 어노테이션을 사용하면 해당 메서드 내의 모든 데이터베이스 작업이 트랜잭션으로 묶여서 처리됩니다.</p>\n" +
                            "<p>예를 들어, 게시글 작성 기능을 구현할 때, 게시글을 작성하고 관련된 데이터를 여러 테이블에 저장해야 할 경우, 트랜잭션을 통해 모든 작업을 하나로 묶을 수 있습니다. 트랜잭션이 성공적으로 완료되면 커밋되고, 예외가 발생하면 롤백됩니다. 트랜잭션을 사용하면, 예외 발생 시 데이터의 불일치 문제를 방지할 수 있습니다. <code>@Transactional</code>은 클래스나 메서드에 적용할 수 있으며, 클래스에 적용하면 클래스 내 모든 메서드에 트랜잭션이 적용됩니다. 메서드에 적용하면 해당 메서드만 트랜잭션을 처리합니다. 이를 통해 서비스의 안정성을 높일 수 있습니다.</p>\n" +
                            "<p>트랜잭션의 기본적인 사용법 외에도, 여러 작업을 동시에 처리하는 병렬 트랜잭션 처리나, 트랜잭션 전파 옵션을 설정하여 트랜잭션의 범위를 조정하는 방법도 있습니다. 예를 들어, <code>REQUIRES_NEW</code> 전파 옵션을 사용하면 새로운 트랜잭션을 생성하여 기존 트랜잭션과 분리하여 처리할 수 있습니다. 이러한 기능은 복잡한 비즈니스 로직을 처리할 때 유용하게 사용됩니다. 또한, 트랜잭션의 격리 수준을 설정하여 데이터베이스의 동시성 처리 방식을 조정할 수 있습니다. 격리 수준을 높이면 여러 트랜잭션이 동시에 처리될 때 발생할 수 있는 문제들을 방지할 수 있습니다.</p>\n" +
                            "<p>트랜잭션을 올바르게 관리하는 것은 데이터 무결성을 유지하는 데 매우 중요하며, 특히 여러 작업을 한 번에 처리하는 경우에 효과적입니다. Spring Boot와 JPA를 이용한 트랜잭션 처리는 다양한 비즈니스 로직을 구현하는 데 중요한 부분이 됩니다. 트랜잭션 처리를 제대로 이해하고 활용하면, 복잡한 시스템에서도 안정적으로 데이터를 처리할 수 있습니다.</p>"));
        };
    }

}