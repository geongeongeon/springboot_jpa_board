package gh.springboot.jpaboard.boundedContext.user;

import gh.springboot.jpaboard.boundedContext.error.DataUnchangedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userRepository")
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Optional<SiteUser> getSiteUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

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

    public void modifyUser(SiteUser siteUser, String password, String email) {
        userRepository.findByEmail(email).ifPresent(duplicateUser -> {
            if (!email.equals(siteUser.getEmail())) {
                throw new DataIntegrityViolationException("duplicateErrorEmail");
            }
        });

        boolean isUpdated = false;

        if (!passwordEncoder.matches(password, siteUser.getPassword())) {
            siteUser.setPassword(passwordEncoder.encode(password));
            isUpdated = true;
        }

        if (!email.equals(siteUser.getEmail())) {
            siteUser.setEmail(email);
            isUpdated = true;
        }

        if (isUpdated) {
            userRepository.save(siteUser);
            updateSecurityContext();
        } else {
            throw new DataUnchangedException("dataUnchangedError");
        }
    }

    private void updateSecurityContext() {
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();

        updateAuthentication(currentAuthentication);
    }

    private void updateAuthentication(Authentication currentAuthentication) {
        Collection<? extends GrantedAuthority> authorities = currentAuthentication.getAuthorities();

        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                currentAuthentication.getPrincipal(),
                currentAuthentication.getCredentials(),
                authorities
        );

        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
    }

}