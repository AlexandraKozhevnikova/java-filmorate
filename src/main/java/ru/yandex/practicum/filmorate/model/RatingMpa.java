package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RatingMpa {
    G("General Audiences", "G", 1),
    PG("Parental Guidance Suggested", "PG", 2),
    PG_13("Parents Strongly Cautioned", "PG-13", 3),
    R("Restricted", "R", 4),
    NC_17("Adults Only", "NC-17", 5);

    private final int id;
    @JsonProperty("name")
    private final String title;
    private final String description;


    RatingMpa(String description, String title, int id) {
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

    public static Optional<RatingMpa> getRatingMpaById(int id) {
        for (RatingMpa mpa : RatingMpa.values()) {
            if (mpa.getId() == id) {
                return Optional.of(mpa);
            }
        }
        return Optional.empty();
    }
}
