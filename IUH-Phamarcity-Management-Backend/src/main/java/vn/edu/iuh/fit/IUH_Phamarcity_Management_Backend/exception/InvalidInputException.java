package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.exception;


public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
