package gh.springboot.jpaboard.boundedContext.admin;

import gh.springboot.jpaboard.boundedContext.answer.AnswerService;
import gh.springboot.jpaboard.boundedContext.error.DataUnchangedException;
import gh.springboot.jpaboard.boundedContext.post.Post;
import gh.springboot.jpaboard.boundedContext.post.PostService;
import gh.springboot.jpaboard.boundedContext.user.SiteUser;
import gh.springboot.jpaboard.boundedContext.user.UserDto;
import gh.springboot.jpaboard.boundedContext.user.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
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
public class AdminController {

    private final AdminService adminService;

    private final PostService postService;

    private final AnswerService answerService;

    @GetMapping("/users")
    public String showUserList(Model model, @RequestParam(defaultValue = "0") int page, String kw) {
        Page<SiteUser> pagedUsers = adminService.getUserList(page, kw);

        for (SiteUser siteUser : pagedUsers) {
            siteUser.setPassword(siteUser.getMaskedPassword());
        }

        model.addAttribute("pagedUsers", pagedUsers);

        return "admin/users";
    }

    @GetMapping("/users/modify/{id}")
    public String showModifyUserForm(UserDto userDto, @PathVariable("id") Long id, Model model) {
        String currentUrl = "/admin/users/modify/%d".formatted(id);

        Optional<SiteUser> optionalSiteUser = adminService.getSiteUserById(id);

        optionalSiteUser.ifPresent(siteUser -> {
            userDto.setUsername(siteUser.getUsername());
            userDto.setEmail(siteUser.getEmail());
            userDto.setRole(siteUser.getRole());

            model.addAttribute("id", id);
            model.addAttribute("currentUrl", currentUrl);
        });

        return "user/modify";
    }

    @PostMapping("/users/modify/{id}")
    public String modifyUser(@Valid UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "user/modify";
        }

        if (userDto.getPassword1().length() < 4 || userDto.getPassword1().length() > 20
                || userDto.getPassword2().length() < 4 || userDto.getPassword2().length() > 20) {
            bindingResult.rejectValue("username", "usernameLengthError", "비밀번호를 4~20글자 사이로 입력해주세요.");

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
        SiteUser user = adminService.getSiteUserById(id).orElseThrow();
        postService.removeAllLikesAndDislikes(user);

        List<Post> userPosts = postService.getPostsByAuthorId(id);

        for (Post post : userPosts) {
            postService.removeAllLikesAndDislikesFromPost(post);
        }

        return adminService.deleteUser(id, loginUser);
    }

}