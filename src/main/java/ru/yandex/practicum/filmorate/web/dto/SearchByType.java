package ru.yandex.practicum.filmorate.web.dto;

public enum SearchByType {
    TITLE("title"),
    DIRECTOR("director");
    private String value;

    SearchByType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
