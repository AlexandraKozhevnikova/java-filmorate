package ru.yandex.practicum.filmorate.web.converter;

import org.springframework.core.convert.converter.Converter;
import ru.yandex.practicum.filmorate.web.dto.SortTypeDirectors;

import java.util.NoSuchElementException;

public class SortTypeDirectorsConverter implements Converter<String, SortTypeDirectors> {
    @Override
    public SortTypeDirectors convert(String source) {
        try {
            return SortTypeDirectors.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException("Wrong type of sort declared");
        }
    }
}

