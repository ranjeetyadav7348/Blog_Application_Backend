package com.blogapplication.application.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blogapplication.application.payloads.apiResponse;

@RestControllerAdvice
public class globalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<apiResponse> ResourceNotFoundException(ResourceNotFoundException e)
    {
        String message=e.getMessage();
        apiResponse ApiResponse =new apiResponse(message, false);
        return new ResponseEntity<apiResponse>(ApiResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> MethodArgumentNotValidException(MethodArgumentNotValidException m)
    {
       Map<String,String> resp=new HashMap<>();

       m.getBindingResult().getAllErrors().forEach((error)->{
        String fieldName=((FieldError) error).getField();
        String message = error.getDefaultMessage();
        resp.put(fieldName,message);
       });



       return new ResponseEntity<Map<String,String>>(resp,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<apiResponse> handleApiException(ApiException e)
    {
        String message=e.getMessage();
        apiResponse ApiResponse =new apiResponse(message, true);
        return new ResponseEntity<apiResponse>(ApiResponse,HttpStatus.BAD_REQUEST);
    }

    
}
