package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.movies.MovieDto;
import cs2024.inf.CineView.services.searchFilmService.SearchFilmService;
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
@RequestMapping("search")
public class SearchFilmController {

    @Autowired
    private SearchFilmService searchFilmService;

    @GetMapping("/title")
    public ResponseEntity<List<MovieDto>> searchMoviesByTitle(@RequestParam String title) {
        List<MovieDto> movies = searchFilmService.searchMoviesByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        MovieDto movieDto = searchFilmService.getMovieById(id);
        return ResponseEntity.status(HttpStatus.OK).body(movieDto);
    }
}
