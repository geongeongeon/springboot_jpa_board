package gh.springboot.jpaboard.boundedContext.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private  final PasswordEncoder passwordEncoder;

    public SiteUser createUser(String username, String password, String email, UserRole role) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(role);

        return userRepository.save(user);
    }
}