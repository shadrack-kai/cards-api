package com.logicea.cards.exception;

import com.logicea.cards.model.dto.response.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CardRequestException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleCardRequestException(CardRequestException ex) {
        return getApiResponse(ex.getCode(), ex.getMessage(), HttpStatus.valueOf(ex.getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        return getApiResponse(400, ex.getMessage(), HttpStatus.valueOf(400));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return getApiResponse(405, ex.getMessage(), HttpStatus.valueOf(405));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Object>> handleAllExceptions(Exception ex) {
        return getApiResponse(500, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiResponseDto<Object>> getApiResponse(int code, String message, HttpStatus status) {
        return new ResponseEntity<>(ApiResponseDto.builder()
                .code(String.valueOf(code))
                .response(message)
                .build(), status);
    }

}
