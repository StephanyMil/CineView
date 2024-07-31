package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.MovieRepository;
import cs2024.inf.CineView.repository.UserRepository;
import cs2024.inf.CineView.services.movieService.MovieService;
import cs2024.inf.CineView.services.movieService.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("movies")
public class MovieController {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;


    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<GenericPageableList> getAllMovies(@RequestParam(value = "page", defaultValue = "0") int page,
                                                            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getAllMovies(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMovieById(@PathVariable(value = "id") long id) {

        return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovieById(id));
    }

    @GetMapping("/{user_id}/recommended")
    public ResponseEntity<Object> getRecommendedMoviesByUser(@PathVariable(value = "user_id") UUID id) {
        try {
            Optional<UserModel> user = userRepository.findById(id);
            if (user.isEmpty()) {
                throw new BusinessException("User not found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(recommendationService.getRecommendedMovies(user.get()));

        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
