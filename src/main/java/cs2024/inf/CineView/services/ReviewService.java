package cs2024.inf.CineView.services;

import cs2024.inf.CineView.dto.ReviewDto;
import cs2024.inf.CineView.models.ReviewModel;
import cs2024.inf.CineView.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public ReviewModel saveReview(ReviewDto reviewDto) {
        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setUsuario(reviewDto.getUsuario());
        reviewModel.setDataAssistido(reviewDto.getDataAssistido());
        reviewModel.setNota(reviewDto.getNota());
        reviewModel.setComentario(reviewDto.getComentario());
        reviewModel.setDataReview(reviewDto.getDataReview());
        reviewModel.setIdFilme(reviewDto.getIdFilme()); // Adiciona essa linha
        return reviewRepository.save(reviewModel);
    }

    public List<ReviewModel> getReviewsByFilmeId(Long idFilme) {
        return reviewRepository.findByIdFilme(idFilme);
    }

    public ReviewModel updateReview(Long id, ReviewDto reviewDto) {
        Optional<ReviewModel> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            ReviewModel reviewModel = optionalReview.get();
            reviewModel.setUsuario(reviewDto.getUsuario());
            reviewModel.setDataAssistido(reviewDto.getDataAssistido());
            reviewModel.setNota(reviewDto.getNota());
            reviewModel.setComentario(reviewDto.getComentario());
            reviewModel.setDataReview(reviewDto.getDataReview());
            reviewModel.setIdFilme(reviewDto.getIdFilme()); // Adiciona essa linha
            return reviewRepository.save(reviewModel);
        }
        return null;
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
