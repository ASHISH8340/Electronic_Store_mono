package com.electronicstore.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private Environment environment;

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> ResponseEntityExceptionHandler(Exception ex, WebRequest request)
            throws Exception {
        logger.info("Inside ResponseEntityExceptionHandler of GlobalExceptionHandler");
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode("500");
        errorDetails.setErrorDetails(ex.getMessage());
        return new ResponseEntity<Object>(new Response<Object>(errorDetails, "1"), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    //MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) throws Exception{
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String,Object> response = new HashMap<>();
        allErrors.stream().forEach(objectError -> {
            String message = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            response.put(field,message);
        });

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BadApiRequest.class)
    public final ResponseEntity<Object> handleBadApiRequest(BadApiRequest ex, WebRequest request)
            throws Exception {
        logger.info("Inside handleBadApiRequest of GlobalExceptionHandler");
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode("400");
        errorDetails.setErrorDetails(ex.getMessage());
        return new ResponseEntity<Object>(new Response<Object>(errorDetails, "1"), HttpStatus.BAD_REQUEST);

    }



}
