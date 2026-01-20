package pl.edu.rezerwacje.advisor_booking.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ IllegalStateException.class, IllegalArgumentException.class })
    public String handleBusinessException(
            RuntimeException ex,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        // komunikat dla użytkownika
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        // wracamy na poprzednią stronę
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
