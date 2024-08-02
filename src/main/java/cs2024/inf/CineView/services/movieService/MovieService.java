package cs2024.inf.CineView.services.movieService;

import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.dto.GenreDto;
import cs2024.inf.CineView.dto.movies.MovieDto;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.models.GenreModel;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.repository.GenreRepository;
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

    @Autowired
    private GenreRepository genreRepository;


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

    @Transactional
    public GenericPageableList searchMoviesByTitle(String title, Long genre_id, Pageable page) {
        List<MovieModel> movieSearchResult = new ArrayList<>();

        if (genre_id != null && genre_id > 0) {
            GenreModel genre = genreRepository.findById(genre_id.intValue()).orElseThrow(() -> new BusinessException("Genre not found with id " + genre_id));
            if (!title.isEmpty()) {
                movieSearchResult = movieRepository.findMovieModelsByTitleContainingIgnoreCaseAndGenreModels(title, genre);
            } else movieSearchResult = movieRepository.findMovieModelsByGenreModels(genre);
        }
        //se tiver só o titulo
        if (!title.isEmpty() && (genre_id == null || genre_id == 0)) {
            movieSearchResult = movieRepository.findByTitleContainingIgnoreCase(title);

        }

        if (movieSearchResult.isEmpty()) {
            throw new BusinessException("No movies found with title containing: " + title);
        }
        List<Object> movieDtos = movieSearchResult.stream().map(this::convertToDTO).collect(Collectors.toList());

        return new GenericPageableList(movieDtos, page);
    }
}
