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
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @PostMapping("/{id_filme}/reviews")
    public ResponseEntity<ReviewModel> saveReview(@PathVariable(value = "id_filme") UUID idFilme, @RequestBody @Valid ReviewDto reviewDto) {
        var reviewModel = new ReviewModel();
        BeanUtils.copyProperties(reviewDto, reviewModel);
        reviewModel.setIdFilme(idFilme);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewRepository.save(reviewModel));
    }

    @GetMapping("/{id_filme}/reviews")
    public ResponseEntity<List<ReviewModel>> getAllReviewsByFilm(@PathVariable(value = "id_filme") UUID idFilme) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewRepository.findByIdFilme(idFilme));
    }

    @GetMapping("/{id_filme}/reviews/{id_review}")
    public ResponseEntity<Object> getReviewById(@PathVariable(value = "id_filme") UUID idFilme, @PathVariable(value = "id_review") UUID idReview) {
        Optional<ReviewModel> reviewFound = reviewRepository.findById(idReview);
        if (reviewFound.isEmpty() || !reviewFound.get().getIdFilme().equals(idFilme)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This review doesn't exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviewFound.get());
    }

    @PutMapping("/{id_filme}/reviews/{id_review}")
    public ResponseEntity<Object> updateReview(@PathVariable(value = "id_filme") UUID idFilme, @PathVariable(value = "id_review") UUID idReview, @RequestBody @Valid ReviewDto reviewDto) {
        Optional<ReviewModel> reviewFound = reviewRepository.findById(idReview);
        if (reviewFound.isEmpty() || !reviewFound.get().getIdFilme().equals(idFilme)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This review doesn't exist");
        }
        var reviewModel = reviewFound.get();
        BeanUtils.copyProperties(reviewDto, reviewModel);
        reviewModel.setId(idReview);
        reviewModel.setIdFilme(idFilme);
        return ResponseEntity.status(HttpStatus.OK).body(reviewRepository.save(reviewModel));
    }

    @DeleteMapping("/{id_filme}/reviews/{id_review}")
    public ResponseEntity<Object> deleteReview(@PathVariable(value = "id_filme") UUID idFilme, @PathVariable(value = "id_review") UUID idReview) {
        Optional<ReviewModel> reviewFound = reviewRepository.findById(idReview);
        if (reviewFound.isEmpty() || !reviewFound.get().getIdFilme().equals(idFilme)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This review doesn't exist");
        }
        reviewRepository.delete(reviewFound.get());
        return ResponseEntity.status(HttpStatus.OK).body("Review was deleted successfully");
    }
}
