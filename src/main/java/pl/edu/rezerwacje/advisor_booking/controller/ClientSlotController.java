package pl.edu.rezerwacje.advisor_booking.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pl.edu.rezerwacje.advisor_booking.entity.*;
import pl.edu.rezerwacje.advisor_booking.repository.*;
import pl.edu.rezerwacje.advisor_booking.security.UserDetailsImpl;
import pl.edu.rezerwacje.advisor_booking.service.AppointmentService;

@Controller
@RequestMapping("/client/slots")
@PreAuthorize("hasRole('CLIENT')")
public class ClientSlotController {

    private final AdvisoryServiceRepository serviceRepository;
    private final TimeSlotRepository slotRepository;
    private final AppointmentService appointmentService;

    public ClientSlotController(
            AdvisoryServiceRepository serviceRepository,
            TimeSlotRepository slotRepository,
            AppointmentService appointmentService) {
        this.serviceRepository = serviceRepository;
        this.slotRepository = slotRepository;
        this.appointmentService = appointmentService;
    }

    // ===============================
    // WYBÓR USŁUGI
    // ===============================
    @GetMapping
    public String chooseService(Model model) {
        model.addAttribute("services", serviceRepository.findAll());
        return "client-slots-services";
    }

    // ===============================
    // LISTA WOLNYCH SLOTÓW
    // ===============================
    @GetMapping("/{serviceId}")
    public String availableSlots(
            @PathVariable Long serviceId,
            Model model) {

        AdvisoryService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Usługa nie istnieje"));

        model.addAttribute("service", service);
        model.addAttribute(
                "slots",
                slotRepository.findByServiceAndStatus(
                        service,
                        SlotStatus.WOLNY));

        return "client-slots-list";
    }

    // ===============================
    // REZERWACJA SLOTU
    // ===============================
    @PostMapping("/{slotId}/reserve")
    public String reserveSlot(
            @PathVariable Long slotId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        appointmentService.bookSlot(slotId, userDetails.getUser());
        return "redirect:/appointments";
    }
}
