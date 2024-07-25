package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.models.GenreModel;
import cs2024.inf.CineView.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("genres")
public class GenreController {
    @Autowired
    GenreRepository genreRepository;

//    @PostConstruct
//    public void init() {
//        if (genreRepository.count() == 0) {
//
//        }
//    }

//    private List<Genre> readGenresCVS() {
//        List<Genre> genres = new ArrayList<>();
//
//    }

    @GetMapping
    public ResponseEntity<List<GenreModel>> getAllGenres() {
        return ResponseEntity.status(HttpStatus.OK).body(genreRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getGenreById(@PathVariable(value = "id") int id) {
        Optional<GenreModel> genre = genreRepository.findById(id);

        return genre.<ResponseEntity<Object>>map(value -> ResponseEntity.status(HttpStatus.OK).body(value)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("This genre doesn't exist in our database"));
    }
}
