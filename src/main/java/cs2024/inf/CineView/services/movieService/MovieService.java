package cs2024.inf.CineView.services.movieService;

import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.dto.GenreDto;
import cs2024.inf.CineView.dto.movies.MovieDto;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.repository.MovieRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;


    @Transactional
    public GenericPageableList getAllMovies(Pageable pageable) {
        Page<MovieModel> movies = movieRepository.findAll(pageable);

        List<Object> moviesDtos = movies.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new GenericPageableList(moviesDtos, pageable);
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


    @Transactional
    public GenericPageableList getPopularMovies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<MovieModel> mostReviewedAndLikedMovies = movieRepository.findMostReviewedAndLikedMovies(PageRequest.of(0, 20));
        List<Object[]> queryResult = movieRepository.findMostSavedInLists();

        List<Long> movieIdList = new ArrayList<>();
        for (Object[] result : queryResult) {
            Long id = ((Number) result[0]).longValue();
            movieIdList.add(id);
        }
        List<MovieModel> mostFavoritedMovies = movieRepository.findAllById(movieIdList);

        List<MovieModel> popularMovies = new ArrayList<>(mostReviewedAndLikedMovies);
        popularMovies.addAll(mostFavoritedMovies);

        List<Object> popularMoviesDtos = popularMovies.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new GenericPageableList(popularMoviesDtos, pageable);
    }
}
