package gh.springboot.jpaboard.boundedContext.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHome(Principal principal, Model model) {
        String username = principal != null ? principal.getName() : null;

        model.addAttribute("username", username);

        return "home";
    }

}
