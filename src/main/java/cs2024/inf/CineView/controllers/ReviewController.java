package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.ReviewDto;
import cs2024.inf.CineView.models.ReviewModel;
import cs2024.inf.CineView.repository.ReviewRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @PostMapping
    public ResponseEntity<ReviewModel> saveReview(@RequestBody @Valid ReviewDto reviewDto) {
        var reviewModel = new ReviewModel();
        BeanUtils.copyProperties(reviewDto, reviewModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewRepository.save(reviewModel));
    }

    @GetMapping
    public ResponseEntity<List<ReviewModel>> getAllReviews() {
        return ResponseEntity.status(HttpStatus.OK).body(reviewRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getReviewById(@PathVariable(value = "id") UUID id) {
        Optional<ReviewModel> reviewFound = reviewRepository.findById(id);
        if (reviewFound.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This review doesn't exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviewFound.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateReview(@PathVariable(value = "id") UUID id, @RequestBody @Valid ReviewDto reviewDto) {
        Optional<ReviewModel> reviewFound = reviewRepository.findById(id);
        if (reviewFound.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This review doesn't exist");
        }
        var reviewModel = reviewFound.get();
        BeanUtils.copyProperties(reviewDto, reviewModel);
        return ResponseEntity.status(HttpStatus.OK).body(reviewRepository.save(reviewModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReview(@PathVariable(value = "id") UUID id) {
        Optional<ReviewModel> reviewFound = reviewRepository.findById(id);
        if (reviewFound.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This review doesn't exist");
        }
        reviewRepository.delete(reviewFound.get());
        return ResponseEntity.status(HttpStatus.OK).body("Review was deleted successfully");
    }
}
