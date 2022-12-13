package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum EventType {
    LIKE(1, "LIKE"),
    REVIEW(2, "REVIEW"),
    FRIEND(3, "FRIEND");

    private final int id;
    private final String name;

    EventType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static EventType getEventTypeByID(int id) {
        for (EventType eventType : EventType.values()) {
            if (eventType.getId() == id) {
                return eventType;
            }
        }
        return null;
    }

}
