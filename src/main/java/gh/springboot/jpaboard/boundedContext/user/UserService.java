package gh.springboot.jpaboard.boundedContext.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void createUser(String username, String password, String email, UserRole role) {
        StringBuilder errors = new StringBuilder();

        userRepository.findByUsername(username).ifPresent(duplicateUser -> {
            errors.append("duplicateErrorUsername");
        });

        userRepository.findByEmail(email).ifPresent(duplicateUser -> {
            errors.append("duplicateErrorEmail");
        });

        if (!errors.isEmpty()) {
            throw new DataIntegrityViolationException(errors.toString());
        }

        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(role);

        userRepository.save(user);
    }

}