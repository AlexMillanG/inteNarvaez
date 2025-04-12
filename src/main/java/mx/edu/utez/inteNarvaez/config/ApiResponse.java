package mx.edu.utez.inteNarvaez.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Getter
@Setter
@NoArgsConstructor
@Transactional(rollbackFor = Exception.class)
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

    public static boolean hasValidationErrors(BindingResult result) {
        return result.hasErrors();
    }

    public static String buildErrorMessage(BindingResult result) {
        StringBuilder sb = new StringBuilder();
        result.getFieldErrors().forEach(error ->
                sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(". ")
        );
        return sb.toString();
    }

    public static ResponseEntity<ApiResponse> buildErrorResponse(BindingResult result) {
        String errorMessage = buildErrorMessage(result);
        return ResponseEntity.badRequest().body(new ApiResponse(null, HttpStatus.BAD_REQUEST, errorMessage, true));
    }
}
