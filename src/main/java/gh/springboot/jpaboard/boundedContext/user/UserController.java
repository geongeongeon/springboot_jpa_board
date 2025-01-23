package gh.springboot.jpaboard.boundedContext.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/create")
    public String showCreateUserForm(CreateUserForm createUserForm) {
        return "user/create";
    }

    @PostMapping("/create")
    public String createUser(@Valid CreateUserForm createUserForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/create";
        }

        if (!createUserForm.getPassword1().equals(createUserForm.getPassword2())) {
            bindingResult.rejectValue("password2", "signupFailed", "비밀번호와 비밀번호 확인이 서로 일치하지 않습니다.");

            return "user/create";
        }

        try {
            userService.createUser(createUserForm.getUsername(), createUserForm.getPassword1(), createUserForm.getEmail(), UserRole.USER);
        } catch (DataIntegrityViolationException e) {
            String[] errors = e.getMessage().split("duplicateError");

            for (String error : errors) {
                if (error.equals("Username")) {
                    createUserForm.setUsername("");

                    bindingResult.reject("signupFailed", "이미 존재하는 아이디입니다.");
                }

                if (error.equals("Email")) {
                    createUserForm.setEmail("");

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
    public String showLoginUserForm() {
        return "user/login";
    }

}