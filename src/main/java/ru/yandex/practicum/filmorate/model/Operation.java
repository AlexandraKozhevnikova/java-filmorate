package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum Operation {
    REMOVE(1, "REMOVE"),
    ADD(2, "ADD"),
    UPDATE(3, "UPDATE");

    private final int id;
    private final String name;

    Operation(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Operation getOperationByID(int id) {
        for (Operation operation : Operation.values()) {
            if (operation.getId() == id) {
                return operation;
            }
        }
        return null;
    }
}
