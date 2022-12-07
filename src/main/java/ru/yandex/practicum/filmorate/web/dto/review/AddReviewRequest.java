package ru.yandex.practicum.filmorate.web.dto.review;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddReviewRequest {
    @NotNull(message = "'content' must not be null")
    private String content;
    @NotNull(message = "'isPositive' must not be null")
    private Boolean isPositive;
    @NotNull(message = "'userId' must not be null")
    private Integer userId;
    @NotNull(message = "'filmId' must not be null")
    private Integer filmId;

    @JsonGetter
    public Boolean getIsPositive() {
        return isPositive;
    }
}
