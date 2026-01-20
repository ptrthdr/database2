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
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/advisor/slots")
@PreAuthorize("hasRole('ADVISOR')")
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
    // FORMULARZ
    // ===============================
    @GetMapping("/new")
    public String newSlotForm(Model model) {
        model.addAttribute("services", serviceRepository.findAll());
        return "slot-form";
    }

    // ===============================
    // ZAPIS SLOTU
    // ===============================
    @PostMapping
    public String addSlot(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("dateTime") LocalDateTime dateTime,
            @RequestParam(value = "serviceIds", required = false) Set<Long> serviceIds,
            Model model) {

        // ===== MUSI BYĆ USŁUGA =====
        if (serviceIds == null || serviceIds.isEmpty()) {
            model.addAttribute("services", serviceRepository.findAll());
            model.addAttribute("errorMessage",
                    "Musisz wybrać co najmniej jedną usługę");
            return "slot-form";
        }

        // ===== DATA NIE MOŻE BYĆ Z PRZESZŁOŚCI =====
        if (dateTime.isBefore(LocalDateTime.now())) {
            model.addAttribute("services", serviceRepository.findAll());
            model.addAttribute("errorMessage",
                    "Nie możesz dodać terminu z przeszłości");
            return "slot-form";
        }

        Advisor advisor = advisorRepository
                .findByUser_Id(userDetails.getUser().getId())
                .orElseThrow();

        Set<AdvisoryService> services = new HashSet<>(serviceRepository.findAllById(serviceIds));

        slotService.addSlot(advisor, services, dateTime);

        // ✅ SUKCES → STRONA GŁÓWNA
        return "redirect:/";
    }
}
