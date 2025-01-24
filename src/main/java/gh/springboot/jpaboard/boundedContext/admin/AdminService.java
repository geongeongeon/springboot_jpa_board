package gh.springboot.jpaboard.boundedContext.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    @Qualifier("userRepository")
    private final AdminRepository adminRepository;

}
