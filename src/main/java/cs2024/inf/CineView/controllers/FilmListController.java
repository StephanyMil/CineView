package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.FilmListDto;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.services.FilmListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/filmLists")
public class FilmListController {

    private final FilmListService filmListService;

    public FilmListController(FilmListService filmListService) {
        this.filmListService = filmListService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<FilmListDto> createFilmList(@RequestBody FilmListDto filmListDto) {
        FilmListDto createdFilmList = filmListService.createFilmList(filmListDto);
        return new ResponseEntity<>(createdFilmList, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping
    public ResponseEntity<List<FilmListDto>> listFilmLists() {
        List<FilmListDto> filmLists = filmListService.findAll();
        return new ResponseEntity<>(filmLists, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<?> getFilmListById(@PathVariable Long id) {
        try {
            FilmListDto filmList = filmListService.findById(id);
            return new ResponseEntity<>(filmList, HttpStatus.OK);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Film list not found.");
        }
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFilmList(@PathVariable Long id, @RequestBody FilmListDto filmListDto) {
        try {
            FilmListDto updatedFilmList = filmListService.updateFilmList(id, filmListDto);
            return new ResponseEntity<>(updatedFilmList, HttpStatus.OK);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Film list not found.");
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFilmList(@PathVariable Long id) {
        try {
            filmListService.deleteFilmList(id);
            return ResponseEntity.ok("Film list deleted successfully.");
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Film list not found.");
        }
    }
}
