package ru.yandex.practicum.filmorate.exception;

public class BadRatingMpaException extends RuntimeException {

    public BadRatingMpaException(String message) {
        super(message);
    }
}
