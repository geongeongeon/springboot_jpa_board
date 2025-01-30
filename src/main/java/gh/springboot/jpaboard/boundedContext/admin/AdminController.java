package gh.springboot.jpaboard.boundedContext.admin;

import gh.springboot.jpaboard.boundedContext.error.DataUnchangedException;
import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserDto;
import gh.springboot.jpaboard.boundedContext.user.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public String showUserList(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<SiteUser> siteUserList =  adminService.getUserList(page);

        for (SiteUser siteUser : siteUserList) {
            siteUser.setPassword(siteUser.getMaskedPassword());
        }

        model.addAttribute("siteUserList", siteUserList.getContent());
        model.addAttribute("paging", siteUserList);

        return "admin/users";
    }

    @GetMapping("/users/modify/{id}")
    public String showModifyUserForm(UserDto userDto, @PathVariable("id") Long id, Model model) {
        Optional<SiteUser> optionalSiteUser = adminService.getSiteUserById(id);

        optionalSiteUser.ifPresent(siteUser -> {
            userDto.setUsername(siteUser.getUsername());
            userDto.setEmail(siteUser.getEmail());
            userDto.setRole(siteUser.getRole());

            model.addAttribute("id", id);
        });

        return "user/modify";
    }

    @PostMapping("/users/modify/{id}")
    public String modifyUser(@Valid UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "user/modify";
        }

        if (!userDto.getPassword1().equals(userDto.getPassword2())) {
            bindingResult.rejectValue("password2", "mismatchPasswordError", "비밀번호와 비밀번호 확인이 서로 일치하지 않습니다.");

            return "user/modify";
        }

        UserRole userRole = userDto.getRole().toString().equals("ADMIN") ? UserRole.ADMIN : UserRole.USER;

        try {
            Optional<SiteUser> optionalSiteUser = adminService.getSiteUserByUsername(userDto.getUsername());

            optionalSiteUser.ifPresent(siteUser -> {
                adminService.modifyUser(siteUser, userDto.getPassword1(), userDto.getEmail(), userRole);
            });

            redirectAttributes.addFlashAttribute("successMsg", "회원 정보가 수정되었습니다.");
        } catch (DataIntegrityViolationException e) {
            userDto.setEmail(userDto.getEmail());

            bindingResult.reject("modifyFailed", "이미 존재하는 이메일입니다.");

            return "user/modify";
        } catch (DataUnchangedException e) {
            bindingResult.reject("modifyFailed", "변경된 정보가 없습니다.");

            return "user/modify";
        } catch (Exception e) {
            bindingResult.reject("modifyFailed", e.getMessage());

            return "user/modify";
        }

        return String.format("redirect:/admin/users/modify/%s", id);
    }

    @PostMapping("/users/delete/{id}")
    private String deleteUser(@PathVariable("id") Long id, Principal loginUser) {
        return adminService.deleteUser(id, loginUser);
    }

}
