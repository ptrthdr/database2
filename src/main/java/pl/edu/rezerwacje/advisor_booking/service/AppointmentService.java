package pl.edu.rezerwacje.advisor_booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.rezerwacje.advisor_booking.entity.*;
import pl.edu.rezerwacje.advisor_booking.repository.*;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository slotRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            TimeSlotRepository slotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
    }

    // ===============================
    // REZERWACJA SLOTU (KLIENT)
    // ===============================
    @Transactional
    public void bookSlot(Long slotId, User client) {

        TimeSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Termin nie istnieje"));

        if (slot.getStatus() != SlotStatus.WOLNY) {
            throw new IllegalStateException("Termin jest już zajęty");
        }

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setSlot(slot);

        slot.setStatus(SlotStatus.ZAREZERWOWANY);
        slot.setAppointment(appointment);

        appointmentRepository.save(appointment);
        slotRepository.save(slot);
    }

    // ===============================
    // LISTA WIZYT KLIENTA
    // ===============================
    public List<Appointment> getAppointmentsForClient(Long clientId) {
        return appointmentRepository.findByClient_Id(clientId);
    }

    // ===============================
    // LISTA WIZYT DORADCY
    // (przez slot -> advisor -> user)
    // ===============================
    public List<Appointment> getAppointmentsForAdvisor(Long advisorUserId) {
        return appointmentRepository
                .findBySlot_Advisor_User_Id(advisorUserId);
    }

    // ===============================
    // ANULOWANIE REZERWACJI
    // ===============================
    @Transactional
    public void cancelAppointment(Long appointmentId, User currentUser) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Wizyta nie istnieje"));

        TimeSlot slot = appointment.getSlot();

        boolean isClient = appointment.getClient().getId().equals(currentUser.getId());

        boolean isAdvisor = slot.getAdvisor().getUser().getId().equals(currentUser.getId());

        if (!isClient && !isAdvisor) {
            throw new SecurityException("Brak uprawnień");
        }

        slot.setStatus(SlotStatus.WOLNY);
        slot.setAppointment(null);

        appointmentRepository.delete(appointment);
        slotRepository.save(slot);
    }
}
