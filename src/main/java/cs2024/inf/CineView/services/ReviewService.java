package cs2024.inf.CineView.services;

import cs2024.inf.CineView.dto.ReviewDto;
import cs2024.inf.CineView.models.ReviewModel;
import cs2024.inf.CineView.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void saveReview(Long id_filme, ReviewDto reviewDto) {
        ReviewModel review = new ReviewModel();
        review.setMovieId(id_filme);
        review.setUsuario(reviewDto.getUsuario());
        review.setDataAssistido(reviewDto.getDataAssistido());
        review.setNota(reviewDto.getNota());
        review.setComentario(reviewDto.getComentario());
        review.setDataReview(reviewDto.getDataReview());
        reviewRepository.save(review);
    }

    public List<ReviewDto> getReviewsByMovieId(Long id_filme) {
        List<ReviewModel> reviews = reviewRepository.findByMovieId(id_filme);
        return reviews.stream()
                .map(review -> new ReviewDto(review.getUsuario(), review.getDataAssistido(),
                        review.getNota(), review.getComentario(), review.getDataReview()))
                .toList();
    }

    public void updateReview(Long id_filme, Long id_review, ReviewDto reviewDto) {
        Optional<ReviewModel> reviewOptional = reviewRepository.findById(id_review);
        if (reviewOptional.isPresent()) {
            ReviewModel review = reviewOptional.get();
            review.setUsuario(reviewDto.getUsuario());
            review.setDataAssistido(reviewDto.getDataAssistido());
            review.setNota(reviewDto.getNota());
            review.setComentario(reviewDto.getComentario());
            review.setDataReview(reviewDto.getDataReview());
            reviewRepository.save(review);
        } else {
            throw new RuntimeException("Review not found");
        }
    }

    public void deleteReview(Long id_filme, Long id_review) {
        reviewRepository.deleteById(id_review);
    }
}
