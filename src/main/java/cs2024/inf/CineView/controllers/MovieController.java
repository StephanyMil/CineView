package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.MovieRepository;
import cs2024.inf.CineView.repository.UserRepository;
import cs2024.inf.CineView.services.UserService;
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

@Controller
@RequestMapping("movies")
public class MovieController {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;


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

    @GetMapping("/recommended")
    public ResponseEntity<Object> getRecommendedMoviesByUser() {
        try {
            UserModel user = userService.getUserByAuth();
            return ResponseEntity.status(HttpStatus.OK).body(recommendationService.getRecommendedMovies(user));

        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/popular-movies")
    public ResponseEntity<GenericPageableList> getPopularMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getPopularMovies(page, size));
    }

}
