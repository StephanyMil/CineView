package cs2024.inf.CineView.dto.movies;

import cs2024.inf.CineView.dto.GenreDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieDto {

    private Long id;
    private String title;
    private String overview;
    private String releaseDate;
    private Double voteAverage;
    private Long voteCount;
    private List<GenreDto> genreModels;

}
