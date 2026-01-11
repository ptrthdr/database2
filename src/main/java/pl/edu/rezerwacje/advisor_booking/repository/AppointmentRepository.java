package pl.edu.rezerwacje.advisor_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.rezerwacje.advisor_booking.entity.Appointment;
import pl.edu.rezerwacje.advisor_booking.entity.Advisor;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // blokada podwójnej rezerwacji
    boolean existsByAdvisorAndAppointmentDateTimeAndStatus_NameIn(
            Advisor advisor,
            LocalDateTime appointmentDateTime,
            List<String> statuses);

    // DLA DORADCY (przez encję Advisor)
    List<Appointment> findByAdvisor(Advisor advisor);

    // DLA KLIENTA
    List<Appointment> findByClientId(Long clientId);
}
