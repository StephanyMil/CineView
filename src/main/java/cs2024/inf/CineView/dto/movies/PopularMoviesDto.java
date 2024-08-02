package cs2024.inf.CineView.dto.movies;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PopularMoviesDto {
    private List<MovieDto> mostLikedReviewedMovies;
    private List<MovieDto> mostSavedListMovies;
}
