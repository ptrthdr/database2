package pl.edu.rezerwacje.advisor_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.rezerwacje.advisor_booking.entity.Appointment;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByClient_Id(Long clientId);

    List<Appointment> findBySlot_Advisor_User_Id(Long advisorUserId);

}
