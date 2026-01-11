package pl.edu.rezerwacje.advisor_booking.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import pl.edu.rezerwacje.advisor_booking.entity.*;
import pl.edu.rezerwacje.advisor_booking.security.UserDetailsImpl;
import pl.edu.rezerwacje.advisor_booking.service.AppointmentService;
import pl.edu.rezerwacje.advisor_booking.repository.*;

import java.time.LocalDateTime;

@Controller
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AdvisorRepository advisorRepository;
    private final AdvisoryServiceRepository advisoryServiceRepository;

    public AppointmentController(
            AppointmentService appointmentService,
            AdvisorRepository advisorRepository,
            AdvisoryServiceRepository advisoryServiceRepository) {
        this.appointmentService = appointmentService;
        this.advisorRepository = advisorRepository;
        this.advisoryServiceRepository = advisoryServiceRepository;
    }

    // ===============================
    // LISTA WIZYT
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
    // NOWA WIZYTA – TYLKO KLIENT
    // ===============================
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/appointments/new")
    public String newAppointmentForm(Model model) {
        model.addAttribute("advisors", advisorRepository.findAll());
        model.addAttribute("services", advisoryServiceRepository.findAll());
        return "appointment-form";
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/appointments")
    public String createAppointment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long advisorId,
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {

        appointmentService.bookAppointment(
                userDetails.getUser(),
                advisorRepository.findById(advisorId).orElseThrow(),
                advisoryServiceRepository.findById(serviceId).orElseThrow(),
                dateTime);

        return "redirect:/appointments";
    }

    // ===============================
    // ANULOWANIE
    // ===============================
    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {

        appointmentService.cancelAppointment(id, userDetails.getUser());
        return "redirect:/appointments";
    }

    // ===============================
    // POTWIERDZENIE – DORADCA
    // ===============================
    @PreAuthorize("hasRole('ADVISOR')")
    @PostMapping("/appointments/{id}/confirm")
    public String confirmAppointment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {

        appointmentService.confirmAppointment(id, userDetails.getUser());
        return "redirect:/appointments";
    }
}
