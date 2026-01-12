package pl.edu.rezerwacje.advisor_booking.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pl.edu.rezerwacje.advisor_booking.entity.*;
import pl.edu.rezerwacje.advisor_booking.repository.*;
import pl.edu.rezerwacje.advisor_booking.security.UserDetailsImpl;
import pl.edu.rezerwacje.advisor_booking.service.TimeSlotService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/slots")
public class TimeSlotController {

    private final TimeSlotService slotService;
    private final AdvisoryServiceRepository serviceRepository;
    private final AdvisorRepository advisorRepository;

    public TimeSlotController(
            TimeSlotService slotService,
            AdvisoryServiceRepository serviceRepository,
            AdvisorRepository advisorRepository) {
        this.slotService = slotService;
        this.serviceRepository = serviceRepository;
        this.advisorRepository = advisorRepository;
    }

    // ===============================
    // FORMULARZ DODAWANIA SLOTU – DORADCA
    // ===============================
    @PreAuthorize("hasRole('ADVISOR')")
    @GetMapping("/new")
    public String newSlotForm(Model model) {
        model.addAttribute("services", serviceRepository.findAll());
        return "slot-form";
    }

    @PreAuthorize("hasRole('ADVISOR')")
    @PostMapping
    public String addSlot(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long serviceId,
            @RequestParam LocalDateTime dateTime) {

        Advisor advisor = advisorRepository
                .findByUser_Id(userDetails.getUser().getId())
                .orElseThrow();

        slotService.addSlot(
                advisor,
                serviceRepository.findById(serviceId).orElseThrow(),
                dateTime);

        return "redirect:/";
    }
}
