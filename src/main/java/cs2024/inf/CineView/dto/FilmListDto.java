package cs2024.inf.CineView.dto;

import cs2024.inf.CineView.dto.movies.MovieDto;
import cs2024.inf.CineView.models.FilmListModel;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FilmListDto {

    private Long id;

    @NotBlank(message = "The name can't be null")
    private String name;

    @NotNull(message = "The user id can't be null")
    private UUID userId;

    @NotNull(message = "The movie list can't be null")
    private List<MovieDto> movies;

    public FilmListDto() {}

    public FilmListDto(FilmListModel filmListModel, List<MovieDto> movieDtos) {
        this.id = filmListModel.getId();
        this.name = filmListModel.getName();
        this.userId = filmListModel.getUser().getId();
        this.movies = movieDtos;
    }
}
