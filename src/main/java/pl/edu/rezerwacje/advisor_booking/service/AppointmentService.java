package pl.edu.rezerwacje.advisor_booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.rezerwacje.advisor_booking.entity.*;
import pl.edu.rezerwacje.advisor_booking.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentStatusRepository statusRepository;
    private final AdvisorRepository advisorRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            AppointmentStatusRepository statusRepository,
            AdvisorRepository advisorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.statusRepository = statusRepository;
        this.advisorRepository = advisorRepository;
    }

    // ===============================
    // REZERWACJA – STATUS OCZEKUJĄCA
    // ===============================
    @Transactional
    public Appointment bookAppointment(
            User client,
            Advisor advisor,
            AdvisoryService service,
            LocalDateTime dateTime) {
        if (appointmentRepository
                .existsByAdvisorAndAppointmentDateTimeAndStatus_NameIn(
                        advisor,
                        dateTime,
                        List.of("ZAREZERWOWANA", "OCZEKUJĄCA"))) {
            throw new IllegalStateException("Termin jest już zajęty");
        }

        AppointmentStatus pending = statusRepository
                .findByName("OCZEKUJĄCA")
                .orElseThrow(() -> new IllegalStateException("Brak statusu OCZEKUJĄCA"));

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setAdvisor(advisor);
        appointment.setService(service);
        appointment.setStatus(pending);
        appointment.setAppointmentDateTime(dateTime);

        return appointmentRepository.save(appointment);
    }

    // ===============================
    // LISTA WIZYT KLIENTA
    // ===============================
    public List<Appointment> getAppointmentsForClient(Long clientId) {
        return appointmentRepository.findByClientId(clientId);
    }

    // ===============================
    // LISTA WIZYT DORADCY (POPRAWIONE)
    // ===============================
    public List<Appointment> getAppointmentsForAdvisor(Long advisorUserId) {

        Advisor advisor = advisorRepository.findByUser_Id(advisorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Doradca nie istnieje"));

        return appointmentRepository.findByAdvisor(advisor);
    }

    // ===============================
    // ANULOWANIE – KLIENT / DORADCA
    // ===============================
    @Transactional
    public void cancelAppointment(Long appointmentId, User currentUser) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wizyty"));

        AppointmentStatus cancelled = statusRepository
                .findByName("ANULOWANA")
                .orElseThrow(() -> new IllegalStateException("Brak statusu ANULOWANA"));

        // klient
        if (appointment.getClient().getId().equals(currentUser.getId())) {

            if (appointment.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("Nie można anulować wizyty w przeszłości");
            }

            appointment.setStatus(cancelled);
            appointmentRepository.save(appointment);
            return;
        }

        // doradca
        if (appointment.getAdvisor().getUser().getId().equals(currentUser.getId())) {
            appointment.setStatus(cancelled);
            appointmentRepository.save(appointment);
            return;
        }

        throw new SecurityException("Brak uprawnień");
    }

    // ===============================
    // POTWIERDZANIE – DORADCA
    // ===============================
    @Transactional
    public void confirmAppointment(Long appointmentId, User currentUser) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wizyty"));

        if (!appointment.getAdvisor().getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Brak uprawnień do potwierdzenia");
        }

        AppointmentStatus confirmed = statusRepository
                .findByName("ZAREZERWOWANA")
                .orElseThrow(() -> new IllegalStateException("Brak statusu ZAREZERWOWANA"));

        appointment.setStatus(confirmed);
        appointmentRepository.save(appointment);
    }
}
