package com.ecommerce.project.exceptions;

import com.ecommerce.project.payload.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends Exception {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String, String> response = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String fieldMessage = err.getDefaultMessage();
            response.put(fieldName, fieldMessage);
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> resourceNotFoundException(ResourceNotFoundException exception){
        String message = exception.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> apiException(APIException exception){
        String message = exception.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
