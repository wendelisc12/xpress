package com.example.xpress.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException e){
        ProblemDetail erroDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage());
        erroDetail.setProperty("bad_request_reason", "Empty Fields");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException e){
        ProblemDetail erroDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage());
        erroDetail.setProperty("bad_request_reason", "Erro fields");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroDetail);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(BadCredentialsException e) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), e.getMessage());
        errorDetail.setProperty("access_denied_reason", "Authentication Failure");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetail);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(TokenExpiredException e) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), e.getMessage());
        errorDetail.setProperty("access_denied_reason", "Not Authorized");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetail);
    }
}
