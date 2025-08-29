package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> {

    private int statusCode;
    private String error;
    private Object message;
    private T data;

    public RestResponse(int statusCode, Object message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public RestResponse(int statusCode, String error, Object message) {
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
    }
}
