package cs2024.inf.CineView.dto;

import cs2024.inf.CineView.models.GenreModel;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record MovieDto(@NotNull String title, @NotNull String overview, @NotNull Date releaseDate,
                       @NotNull float voteAverage, @NotNull int runtime, @NotNull List<GenreModel> genreModels,
                       String tagline) {
}
