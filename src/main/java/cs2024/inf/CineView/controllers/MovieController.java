package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.movies.MovieDto;
import cs2024.inf.CineView.services.movieService.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMovieById(@PathVariable(value = "id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovieById(id));
    }

    // Novo endpoint para buscar filmes pelo título
    @GetMapping("/search")
    public ResponseEntity<List<MovieDto>> searchMoviesByTitle(@RequestParam String title) {
        List<MovieDto> movies = movieService.findMoviesByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }
}
