package mx.edu.utez.inteNarvaez.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {

    private Object data;
    private HttpStatus status;
    private String message;
    private boolean error;

    public ApiResponse(Object object, HttpStatus status, String message, boolean error) {
        this.data = object;
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public ApiResponse(Object object, HttpStatus status, String message) {
        this.data = object;
        this.status = status;
        this.message = message;
    }


}
