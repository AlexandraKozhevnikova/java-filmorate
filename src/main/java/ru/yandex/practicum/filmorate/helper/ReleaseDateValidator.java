package ru.yandex.practicum.filmorate.helper;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * Дата релиза фильма должна быть больше даты рождения кино - 28 декабря 1895 года.
 */
public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, Film> {
    private static LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(Film film, ConstraintValidatorContext constraintValidatorContext) {
        // todo  if (film.getReleaseDate() != null){
        return film.getReleaseDate().isAfter(FIRST_FILM_DATE);
    }
}
