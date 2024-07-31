package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.FilmListDto;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.services.FilmListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;

import java.util.List;

@RestController
@RequestMapping("/filmLists")
public class FilmListController {

    private final FilmListService filmListService;

    public FilmListController(FilmListService filmListService) {
        this.filmListService = filmListService;
    }

    private ResponseEntity<String> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }
        return null;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<?> createFilmList(@RequestBody FilmListDto filmListDto) {
        ResponseEntity<String> authenticationError = checkAuthentication();
        if (authenticationError != null) return authenticationError;

        try {
            FilmListDto createdFilmList = filmListService.createFilmList(filmListDto);
            return new ResponseEntity<>(createdFilmList, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the film list.");
        }
    }

    @Transactional
    @GetMapping
    public ResponseEntity<?> listFilmLists() {
        ResponseEntity<String> authenticationError = checkAuthentication();
        if (authenticationError != null) return authenticationError;

        try {
            List<FilmListDto> filmLists = filmListService.findAll();
            return new ResponseEntity<>(filmLists, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the film lists.");
        }
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<?> getFilmListById(@PathVariable Long id) {
        ResponseEntity<String> authenticationError = checkAuthentication();
        if (authenticationError != null) return authenticationError;

        try {
            FilmListDto filmList = filmListService.findById(id);
            return new ResponseEntity<>(filmList, HttpStatus.OK);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving the film list.");
        }
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFilmList(@PathVariable Long id, @RequestBody FilmListDto filmListDto) {
        ResponseEntity<String> authenticationError = checkAuthentication();
        if (authenticationError != null) return authenticationError;

        try {
            FilmListDto updatedFilmList = filmListService.updateFilmList(id, filmListDto);
            return new ResponseEntity<>(updatedFilmList, HttpStatus.OK);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the film list.");
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFilmList(@PathVariable Long id) {
        ResponseEntity<String> authenticationError = checkAuthentication();
        if (authenticationError != null) return authenticationError;

        try {
            filmListService.deleteFilmList(id);
            return ResponseEntity.ok("Film list deleted successfully.");
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the film list.");
        }
    }
}
