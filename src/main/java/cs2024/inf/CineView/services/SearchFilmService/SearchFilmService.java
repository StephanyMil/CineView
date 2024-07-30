package cs2024.inf.CineView.services.searchFilmService;

import cs2024.inf.CineView.dto.movies.MovieDto;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.repository.MovieRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchFilmService {

    @Autowired
    private MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public List<MovieDto> searchMoviesByTitle(String title) {
        List<MovieModel> movies = movieRepository.findByTitleContainingIgnoreCase(title);
        if (movies.isEmpty()) {
            throw new BusinessException("No movies found with title containing: " + title);
        }
        return movies.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovieDto getMovieById(Long id) {
        Optional<MovieModel> movie = movieRepository.findById(id);
        if (movie.isEmpty()) {
            throw new BusinessException("Movie not found with id: " + id);
        }
        return convertToDTO(movie.get());
    }

    private MovieDto convertToDTO(MovieModel movie) {
        MovieDto movieDto = new MovieDto();
        BeanUtils.copyProperties(movie, movieDto);
        return movieDto;
    }
}
