package gh.springboot.jpaboard.boundedContext.user;

import gh.springboot.jpaboard.boundedContext.error.DataUnchangedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/create")
    public String showCreateUserForm(UserDto userDto, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }

        return "user/create";
    }

    @PostMapping("/create")
    public String createUser(@Validated UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/create";
        }

        if (!userDto.getPassword1().equals(userDto.getPassword2())) {
            bindingResult.rejectValue("password2", "mismatchPasswordError", "비밀번호와 비밀번호 확인이 서로 일치하지 않습니다.");

            return "user/create";
        }

        try {
            userService.createUser(userDto.getUsername(), userDto.getPassword1(), userDto.getEmail(), UserRole.USER);

            redirectAttributes.addFlashAttribute("successMsg", "회원가입이 성공했습니다.");
        } catch (DataIntegrityViolationException e) {
            String[] errors = e.getMessage().split("duplicateError");

            for (String error : errors) {
                if (error.equals("Username")) {
                    userDto.setUsername("");

                    bindingResult.reject("signupFailed", "이미 존재하는 아이디입니다.");
                }

                if (error.equals("Email")) {
                    userDto.setEmail("");

                    bindingResult.reject("signupFailed", "이미 존재하는 이메일입니다.");
                }
            }

            return "user/create";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());

            return "user/create";
        }

        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String showLoginUserForm(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }

        return "user/login";
    }

    @GetMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String showModifyUserForm(Principal principal, UserDto userDto, Model model, HttpServletRequest request) {
        String currentUrl = request.getRequestURL().toString();

        Optional<SiteUser> optLoginUser = userService.getSiteUserByUsername(principal.getName());

        optLoginUser.ifPresent(loginUser -> {
            userDto.setUsername(loginUser.getUsername());
            userDto.setEmail(loginUser.getEmail());

            model.addAttribute("currentUrl", currentUrl);
        });

        return "user/modify";
    }

    @PostMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String modifyUser(@Validated UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/modify";
        }

        if (!userDto.getPassword1().equals(userDto.getPassword2())) {
            bindingResult.rejectValue("password2", "mismatchPasswordError", "비밀번호와 비밀번호 확인이 서로 일치하지 않습니다.");

            return "user/modify";
        }

        try {
            Optional<SiteUser> optionalSiteUser = userService.getSiteUserByUsername(userDto.getUsername());

            optionalSiteUser.ifPresent(siteUser -> {
                userService.modifyUser(siteUser, userDto.getPassword1(), userDto.getEmail());
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

        return "redirect:/user/modify";
    }

}