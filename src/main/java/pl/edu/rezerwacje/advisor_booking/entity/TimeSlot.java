package pl.edu.rezerwacje.advisor_booking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "time_slot", uniqueConstraints = @UniqueConstraint(columnNames = { "advisor_id", "start_date_time" }))
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Advisor advisor;

    @ManyToOne(optional = false)
    private AdvisoryService service;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status;

    @OneToOne
    private Appointment appointment;

    public Long getId() {
        return id;
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

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
