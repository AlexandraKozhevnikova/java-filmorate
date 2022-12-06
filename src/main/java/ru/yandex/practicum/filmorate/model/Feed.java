package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class Feed {
    private int eventId;
    private int entityId;
    private int userId;
    private EventType eventType;
    private Operation operation;
    private Timestamp eventTime;
}
