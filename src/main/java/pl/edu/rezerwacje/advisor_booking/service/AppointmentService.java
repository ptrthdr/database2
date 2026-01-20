package pl.edu.rezerwacje.advisor_booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.rezerwacje.advisor_booking.entity.*;
import pl.edu.rezerwacje.advisor_booking.repository.AppointmentRepository;
import pl.edu.rezerwacje.advisor_booking.repository.TimeSlotRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    // REZERWACJA SLOTU
    // ===============================
    @Transactional
    public void bookSlot(Long slotId, User client) {

        TimeSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Termin nie istnieje"));

        if (slot.getStatus() != SlotStatus.WOLNY) {
            throw new IllegalStateException("Termin jest już zajęty");
        }

        // 🔒 klient nie może mieć dwóch wizyt o tej samej godzinie
        LocalDateTime slotTime = slot.getStartDateTime();

        boolean conflict = appointmentRepository.findByClient_Id(client.getId())
                .stream()
                .anyMatch(a -> a.getSlot() != null &&
                        a.getSlot().getStartDateTime().equals(slotTime));

        if (conflict) {
            throw new IllegalStateException(
                    "Masz już zarezerwowaną wizytę o tej samej godzinie");
        }

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setSlot(slot);

        slot.setStatus(SlotStatus.ZAREZERWOWANY);
        slot.setAppointment(appointment);

        appointmentRepository.save(appointment);
    }

    // ===============================
    // ANULOWANIE
    // ===============================
    @Transactional
    public void cancelAppointment(Long appointmentId, User user) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Wizyta nie istnieje"));

        TimeSlot slot = appointment.getSlot();

        // doradca → zawsze może
        if (user.hasRole("ROLE_ADVISOR")) {
            removeAppointment(slot, appointment);
            return;
        }

        // klient → blokada < 24h
        long hours = Duration.between(
                LocalDateTime.now(),
                slot.getStartDateTime()).toHours();

        if (hours < 24) {
            throw new IllegalStateException(
                    "Nie możesz anulować wizyty na mniej niż 24 godziny przed terminem");
        }

        removeAppointment(slot, appointment);
    }

    private void removeAppointment(TimeSlot slot, Appointment appointment) {
        slot.setStatus(SlotStatus.WOLNY);
        slot.setAppointment(null);
        appointmentRepository.delete(appointment);
    }

    // ===============================
    // FILTROWANIE – DORADCA
    // ===============================
    public List<Appointment> getAppointmentsForAdvisorFiltered(
            Long advisorUserId,
            String status,
            String query) {

        return appointmentRepository.findBySlot_Advisor_User_Id(advisorUserId)
                .stream()
                .filter(a -> status == null || status.isBlank()
                        || a.getSlot().getStatus().name().equals(status))
                .filter(a -> {
                    if (query == null || query.isBlank())
                        return true;

                    String q = query.toLowerCase();

                    return a.getClient().getFirstName().toLowerCase().contains(q)
                            || a.getClient().getLastName().toLowerCase().contains(q)
                            || a.getClient().getId().toString().equals(q);
                })
                .collect(Collectors.toList());
    }

    // ===============================
    // FILTROWANIE – KLIENT
    // ===============================
    public List<Appointment> getAppointmentsForClientFiltered(
            Long clientId,
            String status,
            String query) {

        return appointmentRepository.findByClient_Id(clientId)
                .stream()
                .filter(a -> status == null || status.isBlank()
                        || a.getSlot().getStatus().name().equals(status))
                .filter(a -> {
                    if (query == null || query.isBlank())
                        return true;

                    String q = query.toLowerCase();

                    User advisor = a.getSlot().getAdvisor().getUser();

                    return advisor.getFirstName().toLowerCase().contains(q)
                            || advisor.getLastName().toLowerCase().contains(q)
                            || advisor.getId().toString().equals(q);
                })
                .collect(Collectors.toList());
    }
}
