package pl.edu.rezerwacje.advisor_booking.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.rezerwacje.advisor_booking.entity.SlotStatus;
import pl.edu.rezerwacje.advisor_booking.entity.TimeSlot;
import pl.edu.rezerwacje.advisor_booking.repository.TimeSlotRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TimeSlotStatusScheduler {

    private final TimeSlotRepository slotRepository;

    public TimeSlotStatusScheduler(TimeSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    // co 5 minut
    @Scheduled(fixedRate = 5 * 60 * 1000)
    @Transactional
    public void updateFinishedSlots() {

        List<TimeSlot> expired = slotRepository.findExpiredReservedSlots(LocalDateTime.now());

        for (TimeSlot slot : expired) {
            slot.setStatus(SlotStatus.ZAKONCZONA);
        }
    }
}
