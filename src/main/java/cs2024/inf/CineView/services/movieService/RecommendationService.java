package cs2024.inf.CineView.services.movieService;

import cs2024.inf.CineView.dto.movies.RecommendMovieDto;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.FilmListRepository;
import cs2024.inf.CineView.repository.MovieRepository;
import cs2024.inf.CineView.repository.ReviewRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public List<RecommendMovieDto> getRecommendedMovies(UserModel user) {
        Set<MovieModel> interactedMovies = getInteractedMovies(user.getId());

        Set<Long> keywords = new HashSet<>();
        for (MovieModel movie : interactedMovies) {
            keywords.addAll(movie.getKeywords().stream().map(keyword -> keyword.getId()).collect(Collectors.toSet()));
        }

        Page<MovieModel> recommendedMoviesPage = movieRepository.findMoviesByCommonKeywords(keywords, PageRequest.of(0, 10));

        List<MovieModel> recommendedMovies = new ArrayList<>(recommendedMoviesPage.getContent());
        List<MovieModel> interactedMoviesList = new ArrayList<>(interactedMovies);

        return recommendedMovies.stream().map(movieModel -> convertToDTOList(movieModel)).toList();
    }

    private Set<MovieModel> getInteractedMovies(UUID userId) {
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
}
