package cs2024.inf.CineView.services.movieService;

import cs2024.inf.CineView.dto.GenreDto;
import cs2024.inf.CineView.dto.movies.MovieDto;
import cs2024.inf.CineView.dto.movies.MoviesListDto;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.repository.MovieRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;


    @Transactional
    public MoviesListDto getAllMovies(Pageable pageable) {
        Page<MovieModel> movies = movieRepository.findAll(pageable);

        List<MovieDto> moviesDtos = movies.stream().map(this::convertToDTO).toList();
        return new MoviesListDto(moviesDtos, pageable);
    }


    @Transactional(readOnly = true)
    public Object getMovieById(long id) {
        MovieModel movie = movieRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Movie not found with id " + id));
        return convertToDTO(movie);
    }


    private MovieDto convertToDTO(MovieModel movie) {
        MovieDto movieDto = new MovieDto();
        BeanUtils.copyProperties(movie, movieDto);
        List<GenreDto> genres = movie.getGenreModels().stream()
                .map(movieGenre -> {
                    GenreDto genreDTO = new GenreDto();
                    BeanUtils.copyProperties(movieGenre, genreDTO);
                    return genreDTO;
                }).collect(Collectors.toList());
        movieDto.setGenreModels(genres);
        return movieDto;
    }
}
