package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.web.dto.film.AddFilmRequestDto;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomValidationFilmTest {

    @Test
    @DisplayName("Дата релиза - дата после даты появления кино. Успех")
    public void releaseDateIsValidTest() {
//        Film film = new Film("name", "desc", LocalDate.of(1895, 12, 29), 100);
//
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        Validator validator = factory.getValidator();
//        Set<ConstraintViolation<Film>> violations = validator.validate(film);
//        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Дата релиза равно дата ранее даты появления кино. Ошибка валидации")
    public void releaseDateIsEqualBirthdayCinema() {
        AddFilmRequestDto filmDto = new AddFilmRequestDto();
        filmDto.setName("name");
        filmDto.setDescription("desc");
        filmDto.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmDto.setDuration(100);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<AddFilmRequestDto>> violations = validator.validate(filmDto);
        assertEquals(1, violations.size());
        assertEquals("error release date", violations.stream().findFirst().get().getMessage());
    }

    @Test
    @DisplayName("Дата релиза -  дата ранее даты появления кино. Ошибка валидации")
    public void releaseDateIsAfterBirthdayCinema() {
        AddFilmRequestDto filmDto = new AddFilmRequestDto();
        filmDto.setName("name");
        filmDto.setDescription("desc");
        filmDto.setReleaseDate(LocalDate.of(1895, 12, 27));
        filmDto.setDuration(100);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<AddFilmRequestDto>> violations = validator.validate(filmDto);
        assertEquals(1, violations.size());
        assertEquals("error release date", violations.stream().findFirst().get().getMessage());
    }
}
