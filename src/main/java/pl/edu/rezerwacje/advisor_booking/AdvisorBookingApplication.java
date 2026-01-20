package pl.edu.rezerwacje.advisor_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // ← TO JEST KLUCZ

@EnableScheduling
@SpringBootApplication
public class AdvisorBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvisorBookingApplication.class, args);
	}
}
