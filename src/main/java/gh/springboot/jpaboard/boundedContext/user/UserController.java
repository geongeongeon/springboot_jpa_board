package gh.springboot.jpaboard.boundedContext.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        return "user/create_form";
    }

    @PostMapping("/create")
    public String createUser(@Valid CreateUserForm createUserForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/create_form";
        }

        if (!createUserForm.getPassword1().equals(createUserForm.getPassword2())) {
            return "user/create_form";
        }

        userService.createUser(createUserForm.getUsername(), createUserForm.getPassword1(), createUserForm.getEmail(), UserRole.USER);

        return "redirect:/";
    }

}