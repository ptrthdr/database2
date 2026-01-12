package pl.edu.rezerwacje.advisor_booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.rezerwacje.advisor_booking.entity.*;
import pl.edu.rezerwacje.advisor_booking.repository.TimeSlotRepository;

import java.time.LocalDateTime;

@Service
public class TimeSlotService {

    private final TimeSlotRepository slotRepository;

    public TimeSlotService(TimeSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Transactional
    public void addSlot(
            Advisor advisor,
            AdvisoryService service,
            LocalDateTime dateTime) {
        if (slotRepository.existsByAdvisorAndStartDateTime(advisor, dateTime)) {
            throw new IllegalStateException("Termin już istnieje");
        }

        TimeSlot slot = new TimeSlot();
        slot.setAdvisor(advisor);
        slot.setService(service);
        slot.setStartDateTime(dateTime);
        slot.setStatus(SlotStatus.WOLNY);

        slotRepository.save(slot);
    }
}
