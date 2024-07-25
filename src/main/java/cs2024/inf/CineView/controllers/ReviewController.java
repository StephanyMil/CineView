package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.ReviewDto;
import cs2024.inf.CineView.models.ReviewModel;
import cs2024.inf.CineView.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<ReviewModel> createReview(@RequestBody ReviewDto reviewDto) {
        try {
            ReviewModel review = reviewService.saveReview(reviewDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id_filme}/reviews")
    public ResponseEntity<List<ReviewModel>> getReviewsByFilm(@PathVariable Long id_filme) {
        List<ReviewModel> reviews = reviewService.getReviewsByFilmeId(id_filme);
        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{id_filme}/reviews/{id_review}")
    public ResponseEntity<ReviewModel> updateReview(@PathVariable Long id_review, @RequestBody ReviewDto reviewDto) {
        ReviewModel updatedReview = reviewService.updateReview(id_review, reviewDto);
        if (updatedReview != null) {
            return ResponseEntity.ok(updatedReview);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/{id_filme}/reviews/{id_review}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id_review) {
        try {
            reviewService.deleteReview(id_review);
            return ResponseEntity.status(HttpStatus.OK).body("Review deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        }
    }
}
