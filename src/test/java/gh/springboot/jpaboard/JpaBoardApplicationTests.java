package gh.springboot.jpaboard;

import gh.springboot.jpaboard.boundedContext.admin.AdminService;
import gh.springboot.jpaboard.boundedContext.post.PostRepository;
import gh.springboot.jpaboard.boundedContext.post.PostService;
import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserRepository;
import gh.springboot.jpaboard.boundedContext.user.UserRole;
import gh.springboot.jpaboard.boundedContext.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
class JpaBoardApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostService postService;

	@Autowired
	private MockMvc mockMvc;

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private final String[] adminGetUrls = {
			"/admin/users", "/admin/users/modify/1"
	};

	private final String[] adminPostUrls = {
			"/admin/users/modify/1", "/admin/users/delete/1"
	};

	@Test
	@DisplayName("회원 1명 생성")
	void t001() {
		long countAllUser = userRepository.count();

		userService.createUser("user9999", "9999", "user9999@test.com", UserRole.USER);

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
	@DisplayName("관리자 :: 회원 삭제")
	@WithMockUser(username = "admin", roles = "ADMIN")
	void t007() {
		Optional<SiteUser> findUser = adminService.getSiteUserByUsername("start_user1");

		findUser.ifPresent(user -> {
			Principal loginUser = SecurityContextHolder.getContext().getAuthentication();

			adminService.deleteUser(user.getId(), loginUser);
		});

		findUser = adminService.getSiteUserByUsername("start_user1");

		assertThat(findUser).isEmpty();
	}

	@Test
	@DisplayName("회원 :: 관리자 페이지 접근 실패")
	@WithMockUser(username = "user", roles = "USER")
	void t008() throws Exception {
		for (String url : adminGetUrls) {
			mockMvc.perform(get(url))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/"));
		}

		for (String url : adminPostUrls) {
			mockMvc.perform(post(url))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/"));
		}
	}

	@Test
	@DisplayName("관리자 :: 관리자 페이지 접근 성공")
	@WithMockUser(username = "admin", roles = "ADMIN")
	void t009() throws Exception {
		for (String url : adminGetUrls) {
			mockMvc.perform(get(url))
					.andExpect(status().isOk());
		}

		for (String url : adminPostUrls) {
			mockMvc.perform(post(url))
					.andExpect(status().is3xxRedirection());
		}
	}

	@Test
	@DisplayName("검색어를 포함하는 회원 목록 조회")
	void t010() {
		List<SiteUser> users = userRepository.searchUsersByUsernameOrEmail("start_user");

		assertThat(users.size()).isEqualTo(2);

		assertThat(users.get(0).getUsername()).isEqualTo("start_user1");
		assertThat(users.get(1).getUsername()).isEqualTo("start_user2");
	}

	@Test
	@DisplayName("검색어를 포함하는 페이징된 회원 목록 조회")
	void t011() {
		List<Sort.Order> sortIdDesc = new ArrayList<>();
		sortIdDesc.add(Sort.Order.desc("id"));

		Pageable pageable = PageRequest.of(0, 10, Sort.by(sortIdDesc));

		Page<SiteUser> pagedUsers = userRepository.searchUsersByUsernameOrEmail("start_user", pageable);

		assertThat(pagedUsers.getNumber()).isEqualTo(0);
		assertThat(pagedUsers.getTotalPages()).isEqualTo(1);

		assertThat(pagedUsers.getContent().size()).isEqualTo(2);
		assertThat(pagedUsers.getContent().get(0).getUsername()).isEqualTo("start_user2");
		assertThat(pagedUsers.getContent().get(1).getUsername()).isEqualTo("start_user1");
	}

	@Test
	@DisplayName("회원 100명 생성")
	@Rollback(value = false)
	void t998() {
		long countAllUser = userRepository.count();

		IntStream.rangeClosed(1, 100).forEach(id -> userService.createUser("user%d".formatted(id), "1234", "user%d@test.com".formatted(id), UserRole.USER));

		assertThat(userRepository.count()).isEqualTo(countAllUser + 100L);
	}

	@Test
	@DisplayName("게시글 100개 생성")
	@Rollback(value = false)
	void t999() {
		Optional<SiteUser> optUser = userService.getSiteUserByUsername("start_user1");

		optUser.ifPresent(user -> {
			long countAllPost = postRepository.count();

			IntStream.rangeClosed(1, 100).forEach(id -> postService.writePost(user, "게시글 제목 %d".formatted(id), "게시글 내용 %d".formatted(id)));

			assertThat(postRepository.count()).isEqualTo(countAllPost + 100L);
		});
	}

}