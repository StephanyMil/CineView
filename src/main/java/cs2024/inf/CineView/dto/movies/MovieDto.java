package cs2024.inf.CineView.dto.movies;

import cs2024.inf.CineView.dto.GenreDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MovieDto {

    private Long id;
    private String title;
    private String overview;
    private Date releaseDate;
    private float voteAverage;
    private int runtime;
    private String tagline;
    private Double popularity;
    private Long voteCount;
    private List<GenreDto> genreModels;
}
