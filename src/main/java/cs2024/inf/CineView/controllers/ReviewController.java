package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.dto.ReviewDto;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.models.ReviewModel;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.ReviewRepository;
import cs2024.inf.CineView.services.UserService;
import cs2024.inf.CineView.services.reviewService.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("reviews")
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewService reviewService;

    @Autowired
    UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<Object> editReview(@Valid @RequestBody ReviewDto reviewDto, @PathVariable(value = "id") Long id) {
        try {
            if (reviewRepository.findById(id).isPresent()) {
                reviewDto.setId(id);
                Object reviewDtoSaved = reviewService.editReview(reviewDto);
                return ResponseEntity.status(HttpStatus.OK).body(reviewDtoSaved);
            }
            throw new BusinessException("Review not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<Object> saveReview(@Valid @RequestBody ReviewDto reviewDto) {
        Object reviewDtoSaved = reviewService.postReview(reviewDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewDtoSaved);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Object> likeReview(@PathVariable(value = "id") Long reviewId) {
        try {
            reviewService.incrementLikes(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body("review was liked");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<Object> removeLike(@PathVariable(value = "id") Long reviewId) {
        try {
            reviewService.decreaseLike(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body("The review was desliked");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getReview(@PathVariable(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.findById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getReviewByUser(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        List<ReviewDto> reviews = reviewService.findByUser(PageRequest.of(page, pageSize));
        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("This user doesn't have reviews yet");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping("movie/{id}")
    public ResponseEntity<Object> getMovieReviews(@PathVariable(value = "id") Long id, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        GenericPageableList reviews = reviewService.findMovieReviews(id, PageRequest.of(page, pageSize));
        if (reviews.getResults().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("This user doesn't have reviews yet");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviews() {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReview(@PathVariable(value = "id") Long id) {
        try {
            Optional<ReviewModel> review = reviewRepository.findById(id);
            if (review.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This review does not exist");
            }
            UserModel user = userService.getUserByAuth();
            if (!user.getId().equals(review.get().getUser().getId())) {
                throw new BusinessException("You are not authorized to delete this review");
            }

            reviewRepository.delete(reviewRepository.findById(id).get());
            return ResponseEntity.status(HttpStatus.OK).body("Review deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
