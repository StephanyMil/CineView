package cs2024.inf.CineView.services.movieService;

import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.dto.GenreDto;
import cs2024.inf.CineView.dto.KeywordsDto;
import cs2024.inf.CineView.dto.movies.RecommendMovieDto;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.FilmListRepository;
import cs2024.inf.CineView.repository.MovieRepository;
import cs2024.inf.CineView.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private FilmListRepository filmListRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional
    public GenericPageableList getRecommendedMovies(UserModel user, Pageable pageable) {
        Set<MovieModel> interactedMovies = getInteractedMovies(user.getId());

        if (interactedMovies.isEmpty()) {
            return movieService.getAllMovies(pageable);
        }

        Set<Long> keywords = new HashSet<>();
        for (MovieModel movie : interactedMovies) {
            keywords.addAll(movie.getKeywords().stream().map(keyword -> keyword.getId()).collect(Collectors.toSet()));
        }

        List<MovieModel> recommendedMovies = movieRepository.findMoviesByCommonKeywords(keywords);

        List<MovieModel> interactedMoviesList = new ArrayList<>(interactedMovies);


        recommendedMovies.removeAll(interactedMoviesList);

        List<Object> listRecommendedDto = Collections.singletonList(recommendedMovies.stream().map(this::convertToDTO).toList());

        return new GenericPageableList(listRecommendedDto, pageable);
    }

    @Transactional
    public Set<MovieModel> getInteractedMovies(UUID userId) {
        Set<MovieModel> interactedMovies = new HashSet<>();

        // filmes adicionados recentemente em listas do usuário
        Set<MovieModel> recentUserListMovies = filmListRepository.findRecentByUserId(userId);

        // filmes cujas reviews foram curtidas pelo usuário
        Set<MovieModel> recentLikedReviewMovies = reviewRepository.findLikedMoviesByUserId(userId);

        // filmes que o usuário fez reviews
        Set<Object[]> recentReviewMoviesByUser = reviewRepository.findReviewedMoviesByUserId(userId);
        Set<MovieModel> reviewedMovies = recentReviewMoviesByUser.stream()
                .map(record -> (MovieModel) record[0])
                .collect(Collectors.toSet());

        interactedMovies.addAll(recentUserListMovies);
        interactedMovies.addAll(recentLikedReviewMovies);
        interactedMovies.addAll(reviewedMovies);

        return interactedMovies;
    }

    RecommendMovieDto convertToDTOList(MovieModel movie) {
        RecommendMovieDto movieDto = new RecommendMovieDto();
        BeanUtils.copyProperties(movie, movieDto);
        return movieDto;
    }


    public RecommendMovieDto convertToDTO(MovieModel movie) {
        RecommendMovieDto movieDto = new RecommendMovieDto();
        BeanUtils.copyProperties(movie, movieDto);
        List<GenreDto> genres = movie.getGenreModels().stream()
                .map(movieGenre -> {
                    GenreDto genreDTO = new GenreDto();
                    BeanUtils.copyProperties(movieGenre, genreDTO);
                    return genreDTO;
                }).collect(Collectors.toList());
        movieDto.setGenreModels(genres);
        List<KeywordsDto> keywords = movie.getKeywords().stream()
                .map(movieKeyword -> {
                    KeywordsDto keywordDTO = new KeywordsDto();
                    BeanUtils.copyProperties(movieKeyword, keywordDTO);
                    return keywordDTO;
                }).collect(Collectors.toList());
        movieDto.setKeywords(keywords);

        return movieDto;
    }
}
