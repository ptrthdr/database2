package pl.edu.rezerwacje.advisor_booking.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.rezerwacje.advisor_booking.entity.Role;
import pl.edu.rezerwacje.advisor_booking.entity.User;
import pl.edu.rezerwacje.advisor_booking.repository.RoleRepository;
import pl.edu.rezerwacje.advisor_booking.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {

            // ===== ROLE =====
            Role adminRole = roleRepository
                    .findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role r = new Role();
                        r.setName("ROLE_ADMIN");
                        return roleRepository.save(r);
                    });

            Role clientRole = roleRepository
                    .findByName("ROLE_CLIENT")
                    .orElseGet(() -> {
                        Role r = new Role();
                        r.setName("ROLE_CLIENT");
                        return roleRepository.save(r);
                    });

            Role advisorRole = roleRepository
                    .findByName("ROLE_ADVISOR")
                    .orElseGet(() -> {
                        Role r = new Role();
                        r.setName("ROLE_ADVISOR");
                        return roleRepository.save(r);
                    });

            // ===== ADMIN USER =====
            if (userRepository.findByEmail("admin@test.pl").isEmpty()) {

                User admin = new User();
                admin.setEmail("admin@test.pl");
                admin.setFirstName("Admin");
                admin.setLastName("Test");
                admin.setPassword(passwordEncoder.encode("test"));
                admin.setEnabled(true);

                admin.getRoles().add(adminRole);

                userRepository.save(admin);
            }
        };
    }
}
