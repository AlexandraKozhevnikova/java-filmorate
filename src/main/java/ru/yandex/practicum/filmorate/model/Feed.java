package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Feed {
    private int eventId;
    private int entityId;
    private int userId;
    private String eventType;
    private String operation;
    private Long timestamp;
}
