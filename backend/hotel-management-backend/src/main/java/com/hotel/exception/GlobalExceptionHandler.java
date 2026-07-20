package com.hotel.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hotel.constants.AppMessages;
import com.hotel.dto.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
	        DuplicateResourceException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.CONFLICT.value(),
	            HttpStatus.CONFLICT.getReasonPhrase(),
	            ex.getMessage(),
	            null,
	            request.getRequestURI());

	    return ResponseEntity.status(HttpStatus.CONFLICT)
	            .body(response);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
	        ResourceNotFoundException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            HttpStatus.NOT_FOUND.getReasonPhrase(),
	            ex.getMessage(),
	            null,
	            request.getRequestURI());

	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(response);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
	        MethodArgumentNotValidException ex,
	        HttpServletRequest request) {

	    Map<String, String> errors = new HashMap<>();

	    ex.getBindingResult().getAllErrors().forEach(error -> {

	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();

	        errors.put(fieldName, errorMessage);
	    });

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            HttpStatus.BAD_REQUEST.getReasonPhrase(),
	            AppMessages.VALIDATION_FAILED,
	            errors,
	            request.getRequestURI());

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(
	        AuthorizationDeniedException ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.FORBIDDEN.value(),
	            HttpStatus.FORBIDDEN.getReasonPhrase(),
	            "Access Denied. You do not have permission to access this resource.",
	            null,
	            request.getRequestURI());

	    return ResponseEntity.status(HttpStatus.FORBIDDEN)
	            .body(response);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(
	        Exception ex,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.INTERNAL_SERVER_ERROR.value(),
	            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
	            "Something went wrong. Please try again later.",
	            null,
	            request.getRequestURI());

	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(response);
	}
	
@ExceptionHandler(BadRequestException.class)
public ResponseEntity<ErrorResponse> handleBadRequestException(
        BadRequestException ex,
        HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            ex.getMessage(),
            null,
            request.getRequestURI());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
}

}
