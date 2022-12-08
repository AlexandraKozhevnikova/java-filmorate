package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Feed {
    private int eventId;
    private int entityId;
    private int userId;
    private String eventType;
    private String operation;
    private Timestamp eventTime;
}
