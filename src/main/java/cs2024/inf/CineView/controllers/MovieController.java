package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.movies.MoviesListDto;
import cs2024.inf.CineView.repository.MovieRepository;
import cs2024.inf.CineView.services.movieService.MovieService;
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

    @GetMapping
    public ResponseEntity<MoviesListDto> getAllMovies(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getAllMovies(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMovieById(@PathVariable(value = "id") long id) {

        return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovieById(id));
    }

}
