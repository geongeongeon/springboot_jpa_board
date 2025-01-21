package gh.springboot.jpaboard.boundedContext.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> optFindUser = userRepository.findByUsername(username);

        if (optFindUser.isEmpty()) {
            throw new UsernameNotFoundException("일치하는 회원을 찾을 수 없습니다.");
        }

        SiteUser findUser = optFindUser.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (username.equals("admin")) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getRole()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getRole()));
        }

        return new User(findUser.getUsername(), findUser.getPassword(), authorities);
    }

}
