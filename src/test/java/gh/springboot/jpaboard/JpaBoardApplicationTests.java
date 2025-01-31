package gh.springboot.jpaboard;

import gh.springboot.jpaboard.boundedContext.admin.AdminRepository;
import gh.springboot.jpaboard.boundedContext.admin.AdminService;
import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserRepository;
import gh.springboot.jpaboard.boundedContext.user.UserRole;
import gh.springboot.jpaboard.boundedContext.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JpaBoardApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private AdminService adminService;

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
	@DisplayName("회원 정보 조회")
	void t004() {
		Optional<SiteUser> findUser = userRepository.findById(1L);

		assertThat(findUser).isNotEmpty();

		assertThat(findUser.get().getId()).isEqualTo(1L);
		assertThat(findUser.get().getUsername()).isEqualTo("admin");
		assertThat(passwordEncoder.matches("1234", findUser.get().getPassword())).isTrue();
		assertThat(findUser.get().getEmail()).isEqualTo("admin@test.com");
		assertThat(findUser.get().getRole()).isEqualTo(UserRole.ADMIN);
	}

	@Test
	@DisplayName("회원 :: 회원 정보 변경")
	void t005() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				"start_user1", "1234", UserRole.USER.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		Optional<SiteUser> findUser = userService.getSiteUserByUsername("start_user1");

		findUser.ifPresent(user -> {
			userService.modifyUser(user, "9999", "user9999@test.com");
		});

		findUser = userService.getSiteUserByUsername("start_user1");

		findUser.ifPresent(user -> {
			assertThat(passwordEncoder.matches("9999", user.getPassword())).isTrue();
			assertThat(user.getEmail()).isEqualTo("user9999@test.com");
		});
	}

	@Test
	@DisplayName("관리자 :: 회원 정보 변경")
	@WithMockUser(username = "admin", roles = "ADMIN")
	void t006() {
		Optional<SiteUser> findUser = adminService.getSiteUserByUsername("start_user1");

		findUser.ifPresent(user -> {
			adminService.modifyUser(user, "9999", "user9999@test.com", UserRole.ADMIN);
		});

		findUser = adminService.getSiteUserByUsername("start_user1");

		findUser.ifPresent(user -> {
			assertThat(passwordEncoder.matches("9999", user.getPassword())).isTrue();
			assertThat(user.getEmail()).isEqualTo("user9999@test.com");
			assertThat(user.getAuthorities()).isEqualTo(UserRole.ADMIN.getAuthorities());
		});
	}

	@Test
	@DisplayName("회원 100명 생성")
	@Rollback(value = false)
	void t999() {
		long countAllUser = userRepository.count();

		IntStream.rangeClosed(1, 100).forEach(id -> userService.createUser("user%d".formatted(id), "1234", "user%d@test.com".formatted(id), UserRole.USER));

		assertThat(userRepository.count()).isEqualTo(countAllUser + 100L);
	}

}