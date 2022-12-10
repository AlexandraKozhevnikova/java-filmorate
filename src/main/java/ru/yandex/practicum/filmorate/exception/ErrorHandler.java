package ru.yandex.practicum.filmorate.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleBadFoundResultByIdException(final BadFoundResultByIdException e) {
        log.info("Response status code 404 Not Found {}", e.getMessage());
        return Map.of("logic error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadFriendshipException(final BadFriendshipException e) {
        log.info("Response status code 400 Bad Request {}", e.getMessage());
        return Map.of("logic error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDuplicateKeyException(final DuplicateKeyException e) {
        log.info("Response status code 400 Bad Request {}", e.getMessage());
        return Map.of("object already exist", e.getLocalizedMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchElementException(final NoSuchElementException e) {
        log.info("Response status code 404 Not Found {}", e.getMessage());
        return Map.of("logic error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handleConstraintViolationException(final ConstraintViolationException e) {
        List<String> listError = e.getConstraintViolations().stream()
                .map(it -> it.getMessage())
                .collect(Collectors.toList());
        log.info("Response status code 400 Bad Request {}", e.getMessage());
        return Map.of("validation error", listError);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handleValidationException(final MethodArgumentNotValidException e) {
        List<String> listError = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        log.info("Response status code 400 Bad Request {}", e.getMessage());
        return Map.of("validation error", listError);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgumentException(final IllegalArgumentException e) {
        log.info("Response status code 400 Bad Request {}", e.getMessage());
        return Map.of("validation error", e.getLocalizedMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handleHttpMessageNotReadableException(final MismatchedInputException e) {
        List<String> errorFields = e.getPath().stream()
                .map(it -> it.getFieldName())
                .collect(Collectors.toList());
        log.info("Response status code 400 Bad Request {}", e.getMessage());
        return Map.of("could not parse fields: ", errorFields);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgumentException(final IllegalArgumentException e) {
        log.info("Response status code 400 Bad Request {}", e.getMessage());
        return Map.of("validation error", e.getLocalizedMessage());
    }
}
