package ru.yandex.practicum.filmorate.web.converter;

import org.springframework.core.convert.converter.Converter;
import ru.yandex.practicum.filmorate.web.dto.SortTypeDirectors;

import java.util.Arrays;
import java.util.stream.Collectors;


public class SortTypeDirectorsConverter implements Converter<String, SortTypeDirectors> {
    @Override
    public SortTypeDirectors convert(String source) {
        try {
            return SortTypeDirectors.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("value '" + source + "' does not exist. Try use: " +
                                        Arrays.stream(SortTypeDirectors.values())
                                                .map(SortTypeDirectors::toString)
                                                .collect(Collectors.joining(", ")));
        }
    }
}

