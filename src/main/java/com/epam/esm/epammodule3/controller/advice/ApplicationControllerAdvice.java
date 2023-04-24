package com.epam.esm.epammodule3.controller.advice;

import com.epam.esm.epammodule3.exception.GiftCertificateNotFoundException;
import com.epam.esm.epammodule3.exception.TagAlreadyExistsException;
import com.epam.esm.epammodule3.exception.TagNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTagNotFound(TagNotFoundException exception) {
        log.warn("Cannot find tag: {}", exception.getMessage());
        ErrorResponse responseBody = ErrorResponse.of(exception.getMessage(), 40401);

        return ResponseEntity.status(NOT_FOUND).body(responseBody);
    }

    @ExceptionHandler(TagAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTagAlreadyExists(TagAlreadyExistsException exception) {
        log.warn("Tag already exists: {}", exception.getMessage());
        ErrorResponse responseBody = ErrorResponse.of(exception.getMessage(), 40901);

        return ResponseEntity.status(CONFLICT).body(responseBody);
    }

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGiftCertificateNotFound(GiftCertificateNotFoundException exception) {
        log.warn("Cannot find certificate: {}", exception.getMessage());
        ErrorResponse responseBody = ErrorResponse.of(exception.getMessage(), 40402);

        return ResponseEntity.status(NOT_FOUND).body(responseBody);
    }
}
