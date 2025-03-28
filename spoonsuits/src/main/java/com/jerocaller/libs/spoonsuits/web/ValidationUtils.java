package com.jerocaller.libs.spoonsuits.web;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

public class ValidationUtils {

    public static Map<String, String> getValidationFailedMessage(
        MethodArgumentNotValidException error
    ) {

        Map<String, String> validationFailedMap = new HashMap<>();
        error.getBindingResult().getFieldErrors().forEach(e -> {
            validationFailedMap.put(e.getField(), e.getDefaultMessage());
        });

        return validationFailedMap;
    }

}
