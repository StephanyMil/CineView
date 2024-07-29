package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.dto.ReviewDto;
import cs2024.inf.CineView.repository.ReviewRepository;
import cs2024.inf.CineView.services.reviewService.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("user/{id}")
    public ResponseEntity<Object> getReviewByUser(@PathVariable(value = "id") UUID id, @RequestParam(value = "page") int page, @RequestParam(value = "pageSize") int pageSize) {

        List<ReviewDto> reviews = reviewService.findByUser(id, PageRequest.of(page, pageSize));
        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("This user doesn't have reviews yet");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("movie/{id}")
    public ResponseEntity<Object> getMovieReviews(@PathVariable(value = "id") Long id, @RequestParam(value = "page") int page, @RequestParam(value = "pageSize") int pageSize) {

        GenericPageableList reviews = reviewService.findMovieReviews(id, PageRequest.of(page, pageSize));
//        if (reviews.getResults().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("This user doesn't have reviews yet");
//        }
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
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
