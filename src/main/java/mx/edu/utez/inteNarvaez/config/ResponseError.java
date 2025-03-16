package mx.edu.utez.inteNarvaez.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseError {
    public static ResponseEntity<Map<String, Object>> ResponseErrors(HttpStatus status, String message, String code) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.value());
        errorResponse.put("message", message);
        errorResponse.put("code", code);
        return new ResponseEntity<>(errorResponse, status);
    }
}
