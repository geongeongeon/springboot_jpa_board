package gh.springboot.jpaboard;


import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserRepository;
import gh.springboot.jpaboard.boundedContext.user.UserRole;
import gh.springboot.jpaboard.boundedContext.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class JpaBoardApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void beforeEach() {
		userRepository.deleteAll();

		userRepository.clearIdAutoIncrement();

		SiteUser createStartUser1 = userService.createUser("start_user1", "1234", "start_user1@test.com", UserRole.ADMIN);
		SiteUser createStartUser2 = userService.createUser("start_user2", "1234", "start_user2@test.com", UserRole.USER);
	}

	@Test
	@DisplayName("회원 1명 생성")
	void t001() {
		SiteUser createUser = userService.createUser("user1", "1234", "user1@test.com", UserRole.USER);

		SiteUser findUser =  userRepository.findByUsername("user1");

		assertThat(findUser).isNotNull();

		assertThat(findUser.getId()).isEqualTo(createUser.getId());
		assertThat(findUser.getUsername()).isEqualTo(createUser.getUsername());
		assertThat(findUser.getEmail()).isEqualTo(createUser.getEmail());
		assertThat(findUser.getRole()).isEqualTo(createUser.getRole());
	}

	@Test
	@DisplayName("아이디 중복 예외 발생")
	void t002() {
		SiteUser createUser = userService.createUser("user1", "1234", "user1@test.com", UserRole.USER);

		assertThrows(DataIntegrityViolationException.class, () -> {
			SiteUser duplicateUser = userService.createUser("user1", "1234", "user2@test.com", UserRole.USER);
		});
	}

	@Test
	@DisplayName("이메일 중복 예외 발생")
	void t003() {
		SiteUser createUser = userService.createUser("user1", "1234", "user1@test.com", UserRole.USER);

		assertThrows(DataIntegrityViolationException.class, () -> {
			SiteUser duplicateUser = userService.createUser("user2", "1234", "user1@test.com", UserRole.USER);
		});
	}

}