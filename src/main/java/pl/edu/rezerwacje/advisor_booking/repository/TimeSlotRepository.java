package pl.edu.rezerwacje.advisor_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.rezerwacje.advisor_booking.entity.*;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    boolean existsByAdvisorAndStartDateTime(
            Advisor advisor,
            LocalDateTime startDateTime);

    List<TimeSlot> findByServiceAndStatus(
            AdvisoryService service,
            SlotStatus status);
}
