package mx.edu.utez.inteNarvaez.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.Map;
import org.springframework.ui.Model;

import static mx.edu.utez.inteNarvaez.config.ResponseError.ResponseErrors;

@RestControllerAdvice
public class GlobalErrorController {
/*
    // 404 - Recurso no encontrado
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleNotFound404(NoHandlerFoundException ex) {
        return ResponseErrors(HttpStatus.NOT_FOUND, "¡Ups! El recurso no se encuentra disponible intenta mas tarde", "COE04");
    }

    // 400 - Petición incorrecta
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleBadRequest400(MethodArgumentTypeMismatchException ex) {
        return ResponseErrors(HttpStatus.BAD_REQUEST, "¡Error! Hicite mal la solicitud", "COE40");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleNoResourceFoundException(NoResourceFoundException ex, Model model) {
        model.addAttribute("mensaje", "La URL no existe");
        return ResponseErrors(HttpStatus.NOT_FOUND, "La URL no existe ", "COE41");
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Map<String, Object>> handleNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        return ResponseErrors(HttpStatus.NOT_ACCEPTABLE, "Formato de respuesta no soportado", "COE42");
    }

    // 500 - Error interno del servidor
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleGenericException500(Exception ex) {
        return ResponseErrors(HttpStatus.INTERNAL_SERVER_ERROR, "¡Ups! Algo salió mal, pero no te preocupes, estamos en ello", "COE50");
    }

    // 403 - Acceso prohibido
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, Object>> handleForbidden403(AccessDeniedException ex) {
        return ResponseErrors(HttpStatus.FORBIDDEN, "Acceso denegado ", "COE43");
    }

    // 401 - No autenticado
    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, Object>> handleUnauthorized401(IllegalAccessException ex) {
        return ResponseErrors(HttpStatus.UNAUTHORIZED, "No tienes la autorizacion para continuar", "COE41");
    }

    // 500 - Error en la base de datos
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleDatabaseException500(SQLException ex) {
        return ResponseErrors(HttpStatus.INTERNAL_SERVER_ERROR, "No podemos conectarnos a la DB", "COE05");
    }

 */
}
