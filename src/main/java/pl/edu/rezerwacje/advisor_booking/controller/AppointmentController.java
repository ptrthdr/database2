package pl.edu.rezerwacje.advisor_booking.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    // LISTA + FILTRY
    // ===============================
    @GetMapping("/appointments")
    public String appointments(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String query,
            Model model) {

        User user = userDetails.getUser();

        if (user.hasRole("ROLE_ADVISOR")) {
            model.addAttribute(
                    "appointments",
                    appointmentService.getAppointmentsForAdvisorFiltered(
                            user.getId(), status, query));
        } else {
            model.addAttribute(
                    "appointments",
                    appointmentService.getAppointmentsForClientFiltered(
                            user.getId(), status, query));
        }

        model.addAttribute("status", status);
        model.addAttribute("query", query);

        return "appointments";
    }

    // ===============================
    // ANULOWANIE
    // ===============================
    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            appointmentService.cancelAppointment(id, userDetails.getUser());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/appointments";
    }
}
