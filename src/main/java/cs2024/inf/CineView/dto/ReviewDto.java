package cs2024.inf.CineView.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReviewDto {

    private Long id;
    @NotBlank(message = "the description can't be null")
    private String description;

    @NotNull(message = "the rating can't be null")
    private Double rating;

    @NotNull(message = "the user id can't be null")
    private UUID user_id;

    @NotNull(message = "the movie id can't be null")
    private Long movie_id;

}