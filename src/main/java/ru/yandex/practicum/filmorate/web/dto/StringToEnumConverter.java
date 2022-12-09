package ru.yandex.practicum.filmorate.web.dto;

import org.springframework.core.convert.converter.Converter;

import java.util.NoSuchElementException;

public class StringToEnumConverter implements Converter<String, SortTypeDirectors> {
    @Override
    public SortTypeDirectors convert(String source) {
        try {
            return SortTypeDirectors.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException("Wrong type of sort declared");
        }
    }
}

