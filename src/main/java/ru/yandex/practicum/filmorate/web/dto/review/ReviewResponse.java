package ru.yandex.practicum.filmorate.web.dto.review;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {
    private int reviewId;
    private String content;
    private Boolean isPositive;
    private Integer userId;
    private Integer filmId;
    private Integer useful;

    @JsonGetter
    public Boolean getIsPositive() {
        return isPositive;
    }
}
