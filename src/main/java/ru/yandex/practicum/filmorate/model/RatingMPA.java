package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.yandex.practicum.filmorate.exception.BadFoundResultByIdException;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RatingMPA {
    G("General Audiences", "G", 1),
    PG("Parental Guidance Suggested", "PG", 2),
    PG_13("Parents Strongly Cautioned", "PG-13", 3),
    R("Restricted", "R", 4),
    NC_17("Adults Only", "NC-17", 5);

    private final int id;
    @JsonProperty("name")
    private final String title;
    private final String description;


    RatingMPA(String description, String title, int id) {
        this.description = description;
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public static RatingMPA getRatingMpaById(int id) {
        for (RatingMPA mpa : RatingMPA.values()) {
            if (mpa.getId() == id) {
                return mpa;
            }
        }
        throw new BadFoundResultByIdException("RatingMPA with id = " + id + " does not exist");
    }
}
