package pl.edu.rezerwacje.advisor_booking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===============================
    // KLIENT
    // ===============================
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    // ===============================
    // DORADCA
    // ===============================
    @ManyToOne
    @JoinColumn(name = "advisor_id", nullable = false)
    private Advisor advisor;

    // ===============================
    // USŁUGA (ENCJA!)
    // ===============================
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private AdvisoryService service;

    // ===============================
    // STATUS
    // ===============================
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private AppointmentStatus status;

    // ===============================
    // DATA I GODZINA
    // ===============================
    @Column(name = "appointment_datetime", nullable = false)
    private LocalDateTime appointmentDateTime;

    // ===== GETTERY / SETTERY =====

    public Long getId() {
        return id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Advisor getAdvisor() {
        return advisor;
    }

    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }

    public AdvisoryService getService() {
        return service;
    }

    public void setService(AdvisoryService service) {
        this.service = service;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }
}
