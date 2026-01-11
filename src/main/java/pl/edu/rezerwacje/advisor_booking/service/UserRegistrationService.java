package pl.edu.rezerwacje.advisor_booking.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.rezerwacje.advisor_booking.entity.*;
import pl.edu.rezerwacje.advisor_booking.repository.*;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AdvisorRepository advisorRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            AdvisorRepository advisorRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.advisorRepository = advisorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(
            String email,
            String password,
            String firstName,
            String lastName,
            String accountType,
            String specialization) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Użytkownik już istnieje");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        // 🔑 DOBÓR ROLI NA PODSTAWIE TYPU KONTA
        String roleName = accountType.equals("ADVISOR")
                ? "ROLE_ADVISOR"
                : "ROLE_CLIENT";

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Brak roli: " + roleName));

        user.getRoles().add(role);
        userRepository.save(user);

        // jeśli doradca → wpis do advisors
        if ("ADVISOR".equals(accountType)) {
            Advisor advisor = new Advisor();
            advisor.setUser(user);
            advisor.setSpecialization(specialization);
            advisorRepository.save(advisor);
        }
    }
}
