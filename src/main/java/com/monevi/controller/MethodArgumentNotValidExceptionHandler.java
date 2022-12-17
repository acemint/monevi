package com.monevi.controller;

import com.monevi.dto.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorFields(convertToMap(ex.getBindingResult(), Locale.getDefault()))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private Map<String, String> convertToMap(BindingResult result, Locale locale) {
        if (result.hasFieldErrors()) {
            Map<String, String> map = new HashMap<>();

            for (FieldError fieldError : result.getFieldErrors()) {
                String field = fieldError.getField();

                String errorMessage = messageSource.getMessage(fieldError.getCode(),
                        fieldError.getArguments(), fieldError.getDefaultMessage(), locale);

                if (!map.containsKey(fieldError.getField())) {
                    map.put(field, errorMessage);
                } else {
                    String errors = map.get(field) + ", " + errorMessage;
                    map.put(field, errors);
                }
            }
            return map;
        } else {
            return Collections.emptyMap();
        }
    }
}