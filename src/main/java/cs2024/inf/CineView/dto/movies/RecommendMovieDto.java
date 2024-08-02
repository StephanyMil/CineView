package cs2024.inf.CineView.dto.movies;

import cs2024.inf.CineView.dto.GenreDto;
import cs2024.inf.CineView.dto.KeywordsDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RecommendMovieDto {

    private Long id;
    private String title;
    private String overview;
    private Date releaseDate;
    private float voteAverage;
    private Double popularity;
    private Double vote_average;
    private Long vote_count;
    private List<KeywordsDto> keywords;
    private List<GenreDto> genreModels;

}
