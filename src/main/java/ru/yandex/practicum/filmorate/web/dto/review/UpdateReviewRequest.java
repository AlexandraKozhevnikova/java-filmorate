package ru.yandex.practicum.filmorate.web.dto.review;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateReviewRequest {
    private int reviewId;
    @NotNull(message = "'content' must not be null")
    private String content;
    @NotNull(message = "'isPositive' must not be null")
    private Boolean isPositive;

    @JsonGetter
    public Boolean getIsPositive() {
        return isPositive;
    }
}
