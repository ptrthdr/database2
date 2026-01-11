package pl.edu.rezerwacje.advisor_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.rezerwacje.advisor_booking.entity.AdvisoryService;

public interface AdvisoryServiceRepository
        extends JpaRepository<AdvisoryService, Long> {
}
