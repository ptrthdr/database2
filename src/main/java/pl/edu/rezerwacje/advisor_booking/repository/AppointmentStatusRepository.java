package pl.edu.rezerwacje.advisor_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.rezerwacje.advisor_booking.entity.AppointmentStatus;

import java.util.Optional;

public interface AppointmentStatusRepository
        extends JpaRepository<AppointmentStatus, Long> {

    Optional<AppointmentStatus> findByName(String name);
}
