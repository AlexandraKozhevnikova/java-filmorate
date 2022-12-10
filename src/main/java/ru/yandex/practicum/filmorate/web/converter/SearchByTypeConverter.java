package ru.yandex.practicum.filmorate.web.converter;

import org.springframework.core.convert.converter.Converter;
import ru.yandex.practicum.filmorate.web.dto.SearchByType;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchByTypeConverter implements Converter<String, SearchByType> {
    @Override
    public SearchByType convert(String apiParam) {
        Optional<SearchByType> type = SearchByType.getSearchTypeByApiParam(apiParam);

        return type.orElseThrow(
                () -> new IllegalArgumentException(
                        "value '" + apiParam + "' does not exist. Try use: " +
                                Arrays.stream(SearchByType.values())
                                        .map(SearchByType::getApiParam)
                                        .collect(Collectors.joining(", "))
                )
        );
    }
}

