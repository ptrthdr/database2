package pl.edu.rezerwacje.advisor_booking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===============================
    // KLIENT
    // ===============================
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    // ===============================
    // SLOT CZASOWY
    // ===============================
    @OneToOne(optional = false)
    @JoinColumn(name = "slot_id", unique = true, nullable = false)
    private TimeSlot slot;

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

    public TimeSlot getSlot() {
        return slot;
    }

    public void setSlot(TimeSlot slot) {
        this.slot = slot;
    }
}
