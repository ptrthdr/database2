package pl.edu.rezerwacje.advisor_booking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "time_slot", uniqueConstraints = @UniqueConstraint(columnNames = { "advisor_id", "start_date_time" }))
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== DORADCA =====
    @ManyToOne(optional = false)
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;

    // ===== WIELE USŁUG DLA JEDNEGO TERMINU =====
    @ManyToMany
    @JoinTable(name = "time_slot_services", joinColumns = @JoinColumn(name = "time_slot_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<AdvisoryService> services = new HashSet<>();

    // ===== DATA =====
    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    // ===== STATUS =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status;

    // ===== REZERWACJA =====
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    // ===== GETTERY / SETTERY =====

    public Long getId() {
        return id;
    }

    public Advisor getAdvisor() {
        return advisor;
    }

    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }

    public Set<AdvisoryService> getServices() {
        return services;
    }

    public void setServices(Set<AdvisoryService> services) {
        this.services = services;
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
