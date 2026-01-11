package pl.edu.rezerwacje.advisor_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.rezerwacje.advisor_booking.entity.Advisor;

import java.util.Optional;

public interface AdvisorRepository extends JpaRepository<Advisor, Long> {

    Optional<Advisor> findByUser_Id(Long userId);
}
