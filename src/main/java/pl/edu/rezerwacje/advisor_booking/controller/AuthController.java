package pl.edu.rezerwacje.advisor_booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.rezerwacje.advisor_booking.service.UserRegistrationService;

@Controller
public class AuthController {

    private final UserRegistrationService registrationService;

    public AuthController(UserRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // ===============================
    // LOGOWANIE
    // ===============================
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ===============================
    // FORMULARZ REJESTRACJI
    // ===============================
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    // ===============================
    // OBSŁUGA REJESTRACJI
    // ===============================
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String accountType,
            @RequestParam(required = false) String specialization) {
        registrationService.register(
                email,
                password,
                firstName,
                lastName,
                accountType,
                specialization);

        // po rejestracji → logowanie
        return "redirect:/login";
    }
}
