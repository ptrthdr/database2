package pl.edu.rezerwacje.advisor_booking.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pl.edu.rezerwacje.advisor_booking.security.UserDetailsImpl;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Model model) {
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("roles", userDetails.getAuthorities());
        return "home";
    }
}
