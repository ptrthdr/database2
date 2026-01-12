package pl.edu.rezerwacje.advisor_booking.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pl.edu.rezerwacje.advisor_booking.entity.User;
import pl.edu.rezerwacje.advisor_booking.security.UserDetailsImpl;
import pl.edu.rezerwacje.advisor_booking.service.AppointmentService;

@Controller
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // ===============================
    // LISTA WIZYT
    // klient → swoje
    // doradca → przypisane do niego
    // ===============================
    @GetMapping("/appointments")
    public String appointments(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Model model) {

        User user = userDetails.getUser();

        if (user.hasRole("ROLE_ADVISOR")) {
            model.addAttribute(
                    "appointments",
                    appointmentService.getAppointmentsForAdvisor(user.getId()));
        } else {
            model.addAttribute(
                    "appointments",
                    appointmentService.getAppointmentsForClient(user.getId()));
        }

        return "appointments";
    }

    // ===============================
    // ANULOWANIE
    // klient → tylko OCZEKUJĄCA
    // doradca → zawsze
    // ===============================
    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {

        appointmentService.cancelAppointment(id, userDetails.getUser());
        return "redirect:/appointments";
    }
}
