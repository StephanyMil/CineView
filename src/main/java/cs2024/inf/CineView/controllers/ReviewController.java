package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.ReviewDto;
import cs2024.inf.CineView.repository.ReviewRepository;
import cs2024.inf.CineView.services.reviewService.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("reviews")
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Object> saveReview(@Valid @RequestBody ReviewDto reviewDto) {
        Object reviewDtoSaved = reviewService.saveReview(reviewDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewDtoSaved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getReview(@PathVariable(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviews() {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.findAll());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReview(@PathVariable(value = "id") Long id) {
        if (!reviewRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This review does not exist");
        }

        reviewRepository.delete(reviewRepository.findById(id).get());
        return ResponseEntity.status(HttpStatus.OK).body("Review deleted successfully");
    }

}
