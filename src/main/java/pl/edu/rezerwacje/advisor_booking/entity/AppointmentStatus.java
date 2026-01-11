package pl.edu.rezerwacje.advisor_booking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "appointment_statuses")
public class AppointmentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // GETTERY / SETTERY

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
