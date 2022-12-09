package ru.yandex.practicum.filmorate.web.dto;

import java.util.Optional;

public enum SearchByType {
    NAME("title"),
    DIRECTOR("director");
    private String apiParam;

    SearchByType(String apiParam) {
        this.apiParam = apiParam;
    }

    public String getApiParam() {
        return apiParam;
    }

    public static Optional<SearchByType> getSearchTypeByApiParam(String apiParam) {
        for (SearchByType type : SearchByType.values()) {
            if (type.getApiParam().equals(apiParam)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
