package cs2024.inf.CineView.dto.movies;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
public class MoviesListDto {
    private List<MovieDto> movies;
    private Pageable page;

    public MoviesListDto(List<MovieDto> movies, Pageable page) {
        this.movies = movies;
        this.page = page;
    }

}
