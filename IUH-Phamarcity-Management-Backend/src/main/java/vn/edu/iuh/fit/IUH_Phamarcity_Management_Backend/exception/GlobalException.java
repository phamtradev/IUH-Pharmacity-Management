package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInputException(InvalidInputException invalidInputException) {
        return ResponseEntity.badRequest().body(invalidInputException.getMessage());
    }
}
