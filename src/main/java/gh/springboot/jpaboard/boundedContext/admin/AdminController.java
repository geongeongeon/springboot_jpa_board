package gh.springboot.jpaboard.boundedContext.admin;

import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public String showUserList(Model model) {
        List<SiteUser> siteUserList =  adminService.getUserList();

        for (SiteUser siteUser : siteUserList) {
            siteUser.setPassword(siteUser.getMaskedPassword());
        }

        model.addAttribute("siteUserList", siteUserList);

        return "admin/users";
    }

}
