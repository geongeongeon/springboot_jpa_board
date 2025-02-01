package gh.springboot.jpaboard.boundedContext.admin;

import gh.springboot.jpaboard.boundedContext.error.DataUnchangedException;
import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserRepository;
import gh.springboot.jpaboard.boundedContext.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Page<SiteUser> getUserList(int page, String kw) {
        List<Sort.Order> sortIdDesc = new ArrayList<>();
        sortIdDesc.add(Sort.Order.desc("id"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortIdDesc));

        if (kw == null || kw.trim().isEmpty()) {
            return userRepository.findAll(pageable);
        }

        return userRepository.searchUsersByUsernameOrEmail(kw, pageable);
    }

    public Optional<SiteUser> getSiteUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<SiteUser> getSiteUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void modifyUser(SiteUser siteUser, String password, String email, UserRole role) {
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

        if (!role.equals(siteUser.getRole())) {
            siteUser.setRole(role);
            isUpdated = true;
        }

        if (isUpdated) {
            userRepository.save(siteUser);
            updateSecurityContext(siteUser);
        } else {
            throw new DataUnchangedException("dataUnchangedError");
        }
    }

    public String deleteUser(Long id, Principal loginUser) {
        Optional<SiteUser> optDeleteUser = userRepository.findById(id);

        if (optDeleteUser.isPresent()) {
            userRepository.deleteById(id);

            if (optDeleteUser.get().getUsername().equals(loginUser.getName())) {
                return "redirect:/user/logout";
            }
        }

        return "redirect:/admin/users";
    }

    private void updateSecurityContext(SiteUser siteUser) {
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();

        if (siteUser.getUsername().equals(currentAuthentication.getName())
                && !siteUser.getAuthorities().equals(currentAuthentication.getAuthorities())) {
            updateAuthentication(currentAuthentication, siteUser.getAuthorities());
        } else {
            updateAuthentication(currentAuthentication, currentAuthentication.getAuthorities());
        }
    }

    private void updateAuthentication(Authentication currentAuthentication, Collection<? extends GrantedAuthority> authorities) {
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                currentAuthentication.getPrincipal(),
                currentAuthentication.getCredentials(),
                authorities
        );

        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
    }

}