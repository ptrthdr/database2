package pl.edu.rezerwacje.advisor_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.edu.rezerwacje.advisor_booking.entity.*;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    boolean existsByAdvisorAndStartDateTime(
            Advisor advisor,
            LocalDateTime startDateTime);

    // ✅ tylko przyszłe wolne sloty
    @Query("""
                SELECT DISTINCT s FROM TimeSlot s
                JOIN s.services srv
                WHERE srv.id = :serviceId
                AND s.status = pl.edu.rezerwacje.advisor_booking.entity.SlotStatus.WOLNY
                AND s.startDateTime > CURRENT_TIMESTAMP
            """)
    List<TimeSlot> findFreeSlotsByService(@Param("serviceId") Long serviceId);

    // ✅ sloty do oznaczenia jako ZAKONCZONA
    @Query("""
                SELECT s FROM TimeSlot s
                WHERE s.status = pl.edu.rezerwacje.advisor_booking.entity.SlotStatus.ZAREZERWOWANY
                AND s.startDateTime < :now
            """)
    List<TimeSlot> findExpiredReservedSlots(@Param("now") LocalDateTime now);
}
