package gh.springboot.jpaboard.boundedContext.admin;

import gh.springboot.jpaboard.boundedContext.error.DataUnchangedException;
import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    @Qualifier("userRepository")
    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    public List<SiteUser> getUserList() {
        return adminRepository.findAll();
    }

    public Optional<SiteUser> getSiteUserById(Long id) {
        return adminRepository.findById(id);
    }

    public Optional<SiteUser> getSiteUserByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public void modifyUser(SiteUser siteUser, String password, String email, UserRole role) {
        adminRepository.findByEmail(email).ifPresent(duplicateUser -> {
            throw new DataIntegrityViolationException("duplicateErrorEmail");
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

        if (!role.equals(siteUser.getRole())) {
            siteUser.setRole(role);
            isUpdated = true;
        }

        if (isUpdated) {
            adminRepository.save(siteUser);
            updateSecurityContext(siteUser);
        } else {
            throw new DataUnchangedException("dataUnchangedError");
        }
    }

    private void updateSecurityContext(SiteUser siteUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                siteUser.getUsername(),
                siteUser.getPassword(),
                siteUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
