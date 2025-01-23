package gh.springboot.jpaboard;


import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserRepository;
import gh.springboot.jpaboard.boundedContext.user.UserRole;
import gh.springboot.jpaboard.boundedContext.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class JpaBoardApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Test
	@DisplayName("회원 1명 생성")
	void t001() {
		long countAllUser = userRepository.count();

		userService.createUser("user1", "1234", "user1@test.com", UserRole.USER);

		assertThat(userRepository.count()).isEqualTo(countAllUser + 1L);
	}

	@Test
	@DisplayName("아이디 중복 예외 발생")
	void t002() {
		assertThrows(DataIntegrityViolationException.class, () -> {
			userService.createUser("start_user1", "1234", "user1@test.com", UserRole.USER);
		});
	}

	@Test
	@DisplayName("이메일 중복 예외 발생")
	void t003() {
		assertThrows(DataIntegrityViolationException.class, () -> {
			userService.createUser("user1", "1234", "start_user1@test.com", UserRole.USER);
		});
	}

	@Test
	@DisplayName("1번 회원 데이터 조회")
	void t004() {
		Optional<SiteUser> findUser = userRepository.findById(1L);

		assertThat(findUser).isNotEmpty();

		assertThat(findUser.get().getId()).isEqualTo(1L);
		assertThat(findUser.get().getUsername()).isEqualTo("admin");
		assertThat(passwordEncoder.matches("1234", findUser.get().getPassword())).isTrue();
		assertThat(findUser.get().getEmail()).isEqualTo("admin@test.com");
		assertThat(findUser.get().getRole()).isEqualTo(UserRole.ADMIN);
	}

}