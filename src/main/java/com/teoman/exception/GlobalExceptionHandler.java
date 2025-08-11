package com.teoman.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RejectedInvoiceException.class)
    public ResponseEntity<?> handleRejected(RejectedInvoiceException ex) {
        return ResponseEntity.unprocessableEntity().body(Map.of(
                "status", 422,
                "error", "Unprocessable Entity",
                "message", ex.getMessage(),
                "invoiceId", ex.getInvoiceId(),
                "billNo", ex.getBillNo(),
                "approved", false
                                                               ));
    }


    // 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 422 – iş kuralı
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<?> handleBusiness(BusinessRuleException ex) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    // 400 – @Valid hataları (kısa özet)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                       .findFirst()
                       .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                       .orElse("Validation failed");
        return build(HttpStatus.BAD_REQUEST, msg);
    }

    // 500 – genel
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Beklenmeyen bir hata: " + ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
                                                        ));
    }
}
