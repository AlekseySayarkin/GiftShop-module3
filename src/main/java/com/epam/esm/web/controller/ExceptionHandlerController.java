package com.epam.esm.web.controller;

import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<PersistenceException> handleServiceException(PersistenceException exception) {
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<PersistenceException> handleServiceException() {
        PersistenceException exception = new PersistenceException("Invalid field type passed in a JSON",
                ErrorCodeEnum.INVALID_INPUT);
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}
