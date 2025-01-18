package gh.springboot.jpaboard.boundedContext.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public SiteUser createUser(String name, String password, String email, UserRole role) {
        SiteUser user = new SiteUser();
        user.setUsername(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(role);

        return userRepository.save(user);
    }
}