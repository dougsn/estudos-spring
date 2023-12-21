package com.estudos.controller.exceptions;

import com.estudos.services.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ResourceExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    // Generic exceptions
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<StandardError> handleUnexpectedException(HttpServletRequest request) {
        StandardError error =
                new StandardError(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Erro inesperado, verifique os logs.", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<StandardError> handleExceptionHttpClient(HttpServletRequest request, HttpClientErrorException ex) {
        StandardError error =
                new StandardError(LocalDateTime.now(), ex.getStatusCode().value(),
                        ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(ex.getStatusCode()).body(error);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> httpMessageNotReadableException(HttpServletRequest request) {
        StandardError error =
                new StandardError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Corpo da requisição é obrigatório.", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<StandardError>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<StandardError> dto = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(err -> {
            String message = messageSource.getMessage(err, LocaleContextHolder.getLocale());
            StandardError error = new StandardError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), message, request.getRequestURI());
            dto.add(error);
        });

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    // System access exceptions (Security)
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<StandardError> accessDeniedException(HttpServletRequest request) {
//        StandardError error =
//                new StandardError(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(), "Acesso negado.", request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
//    }
//
//    @ExceptionHandler(MalformedJwtException.class)
//    public ResponseEntity<StandardError> malformedJwtException(HttpServletRequest request) {
//        StandardError error =
//                new StandardError(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(), "JWT malformado.", request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
//    }
//
//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<StandardError> expiredJwtException(HttpServletRequest request) {
//        StandardError error =
//                new StandardError(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(), "JWT expirado.", request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
//    }
//
//    @ExceptionHandler(SignatureException.class)
//    public ResponseEntity<StandardError> signatureException(HttpServletRequest request) {
//        StandardError error =
//                new StandardError(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), "Assinatura do JWT inválida.", request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
//    }

    // Exceptions handled and used in the service layer.

//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<StandardError> badCredentialsException(HttpServletRequest request) {
//        StandardError error =
//                new StandardError(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), "Credenciais incorretas.", request.getRequestURI());
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
//    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFoundException(ObjectNotFoundException ex, HttpServletRequest request) {
        StandardError error =
                new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DataIntegratyViolationException.class)
    public ResponseEntity<StandardError> dataIntegratyViolationException(DataIntegratyViolationException ex, HttpServletRequest request) {
        StandardError error =
                new StandardError(LocalDateTime.now(), HttpStatus.CONFLICT.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EmptyAttributeException.class)
    public ResponseEntity<StandardError> emptyAttributeException(EmptyAttributeException ex, HttpServletRequest request) {
        StandardError error =
                new StandardError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardError> badRequestException(BadRequestException ex, HttpServletRequest request) {
        StandardError error =
                new StandardError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CascadingDataBreachException.class)
    public ResponseEntity<StandardError> dataCascadingBreachExceptionException(CascadingDataBreachException ex, HttpServletRequest request) {
        StandardError error =
                new StandardError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
