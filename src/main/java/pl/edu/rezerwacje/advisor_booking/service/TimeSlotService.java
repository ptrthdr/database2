package pl.edu.rezerwacje.advisor_booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.rezerwacje.advisor_booking.entity.*;
import pl.edu.rezerwacje.advisor_booking.repository.TimeSlotRepository;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class TimeSlotService {

    private final TimeSlotRepository slotRepository;

    public TimeSlotService(TimeSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Transactional
    public void addSlot(
            Advisor advisor,
            Set<AdvisoryService> services,
            LocalDateTime dateTime) {

        // ❌ TERMIN W PRZESZŁOŚCI
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Nie możesz dodać terminu z przeszłości");
        }

        // ❌ BRAK USŁUG
        if (services == null || services.isEmpty()) {
            throw new IllegalStateException("Musisz wybrać co najmniej jedną usługę");
        }

        // ❌ DUPLIKAT GODZINY DLA DORADCY
        if (slotRepository.existsByAdvisorAndStartDateTime(advisor, dateTime)) {
            throw new IllegalStateException("Masz już termin o tej godzinie");
        }

        TimeSlot slot = new TimeSlot();
        slot.setAdvisor(advisor);
        slot.setStartDateTime(dateTime);
        slot.setServices(services);
        slot.setStatus(SlotStatus.WOLNY);

        slotRepository.save(slot);
    }
}
