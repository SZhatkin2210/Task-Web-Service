package org.example.spring_boot_2.Errors;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e){
        log.error("handleException", e);
        ErrorResponseDto responseDto = new ErrorResponseDto(
                e.getMessage(),
                "Общая ошибка",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFound(EntityNotFoundException e){
        log.error("handleEntityNotFound", e);
        ErrorResponseDto responseDto = new ErrorResponseDto(
                e.getMessage(),
                "Not found",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseDto);
    }

    @ExceptionHandler(exception = {
            IllegalArgumentException.class,
            IllegalStateException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(Exception e){
        log.error("handleBadRequest", e);
        ErrorResponseDto responseDto = new ErrorResponseDto(
                e.getMessage(),
                "Bad Request",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseDto);
    }


}
