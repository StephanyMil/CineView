package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.ReviewDto;
import cs2024.inf.CineView.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/{id_filme}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // HU 11: Cadastrar review
    @PostMapping
    public ResponseEntity<Void> createReview(@PathVariable Long id_filme,
                                             @RequestBody @Valid ReviewDto reviewDto) {
        reviewService.saveReview(id_filme, reviewDto);
        return ResponseEntity.ok().build();
    }

    // HU 12: Visualizar reviews de outros usuários
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviewsByMovieId(@PathVariable Long id_filme) {
        List<ReviewDto> reviews = reviewService.getReviewsByMovieId(id_filme);
        return ResponseEntity.ok(reviews);
    }

    // HU 14: Editar review
    @PutMapping("/{id_review}")
    public ResponseEntity<Void> updateReview(@PathVariable Long id_filme,
                                             @PathVariable Long id_review,
                                             @RequestBody @Valid ReviewDto reviewDto) {
        reviewService.updateReview(id_filme, id_review, reviewDto);
        return ResponseEntity.ok().build();
    }

    // HU 15: Excluir review
    @DeleteMapping("/{id_review}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id_filme,
                                             @PathVariable Long id_review) {
        reviewService.deleteReview(id_filme, id_review);
        return ResponseEntity.noContent().build();
    }
}
