package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.exception.BadRatingMpaException;

public enum RatingMPA {
    G("General Audiences", 1),
    PG("Parental Guidance Suggested", 2),
    PG_13("Parents Strongly Cautioned", 3),
    R("Restricted", 4),
    NC_17("Adults Only", 5);

    private final String description;
    private final int id;

    RatingMPA(String description, int id) {
        this.description = description;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public static RatingMPA getRatingMpaById(int id) {
        for (RatingMPA mpa : RatingMPA.values()) {
            if (mpa.getId() == id){
                return mpa;
            }
        }
        throw new BadRatingMpaException("RatingMPA with id = " + id + "does not exist");
    }
}

