package mx.edu.utez.intenarvaez.config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;


@RestControllerAdvice
public class GlobalErrorController {

    private static final Logger logger = LogManager.getLogger(GlobalErrorController.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse> handleNotFound404(NoHandlerFoundException ex) {
        logger.error("Error 404: {}", ex.getMessage());

        return new ResponseEntity<>(
                new ApiResponse(null, HttpStatus.NOT_FOUND, "¡Ups! El recurso no se encuentra disponible, intenta más tarde", true),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleBadRequest400(MethodArgumentTypeMismatchException ex) {
        logger.error("Error 400: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ApiResponse(null, HttpStatus.BAD_REQUEST, "¡Error! Hiciste mal la solicitud", true),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse> handleNoResourceFoundException(NoResourceFoundException ex, Model model) {
        logger.error("Error 404 : La URL no existe {}", ex.getMessage());
        model.addAttribute("mensaje", "La URL no existe");
        return new ResponseEntity<>(
                new ApiResponse(null, HttpStatus.NOT_FOUND, "La URL no existe", true),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<ApiResponse> handleNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        logger.error("Error 406: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ApiResponse(null, HttpStatus.NOT_ACCEPTABLE, "Formato de respuesta no soportado", true),
                HttpStatus.NOT_ACCEPTABLE
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse> handleGenericException500(Exception ex) {
        logger.error("Error 500: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "¡Ups! Algo salió mal, pero no te preocupes, estamos en ello", true),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiResponse> handleForbidden403(AccessDeniedException ex) {
        logger.error("Error 403: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ApiResponse(null, HttpStatus.FORBIDDEN, "Acceso denegado", true),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse> handleUnauthorized401(IllegalAccessException ex) {
        logger.error("Error 401: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ApiResponse(null, HttpStatus.UNAUTHORIZED, "No tienes la autorización para continuar", true),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse> handleDatabaseException500(SQLException ex) {
        logger.error("Error 500 : No podemos conectarnos a la base de datos {}", ex.getMessage());
        return new ResponseEntity<>(
                new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "No podemos conectarnos a la base de datos", true),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


}
