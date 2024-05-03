package in.snm.restservice.handler;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest webRequest) {
        String requestUri = ((ServletWebRequest)webRequest).getRequest().getRequestURI();
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError.getField() + " " + fieldError.getDefaultMessage();
        ExceptionResponse exceptionResponse = new ExceptionResponse(errorMessage,requestUri,"Validation Error", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex, WebRequest webRequest) {
        String requestUri = ((ServletWebRequest)webRequest).getRequest().getRequestURI();
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),requestUri,"Unauthorized", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionResponse> handleDataAccessException(DataAccessException ex,  WebRequest webRequest) {
        ex.printStackTrace();
        String requestUri = ((ServletWebRequest)webRequest).getRequest().getRequestURI();
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),requestUri,"An error occurred while accessing data", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    public ResponseEntity<ExceptionResponse> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI();
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),requestUri,"Token expired", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ExceptionResponse> handleException(Exception ex,  WebRequest webRequest) {
        ex.printStackTrace();
        String requestUri = ((ServletWebRequest)webRequest).getRequest().getRequestURI();
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),requestUri,"Internal server error", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}