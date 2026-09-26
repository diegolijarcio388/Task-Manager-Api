package com.diego.task_manager_api.exception;

import org.apache.coyote.Response;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestiona de forma global las excepciones producidas por la API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Captura los errores producidos al validar los datos de entrada
     * y devuelve un mapa con el campo incorrecto y su mensaje de error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException exception) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors =
                exception.getBindingResult().getFieldErrors();

        for (FieldError error : fieldErrors) {
            errors.put(
                    error.getField(),
                    error.getDefaultMessage()
            );
        }
        problem.setTitle("Error de validación");
        problem.setDetail("Uno o más campos no son válidos");
        problem.setProperty("errors", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problem);

    }

    /**
     * Redirigir un error con 404 Not Found
     */

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleTaskNotFound(TaskNotFoundException exception) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Tarea no encontrada");
        problem.setDetail(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleNotReadableException(HttpMessageNotReadableException exception){
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Petición no válida");
        problem.setDetail("El cuerpo de la petición no se puede interpretar");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problem);

    }

}
