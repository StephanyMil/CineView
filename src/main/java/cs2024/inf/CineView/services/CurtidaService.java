package cs2024.inf.CineView.services;

import cs2024.inf.CineView.dto.CurtidaDto;
import cs2024.inf.CineView.models.CurtidaModel;
import cs2024.inf.CineView.models.ReviewModel;
import cs2024.inf.CineView.repository.CurtidaRepository;
import cs2024.inf.CineView.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurtidaService {

    @Autowired
    private CurtidaRepository curtidaRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public CurtidaModel saveCurtida(Long reviewId, CurtidaDto curtidaDto) {
        Optional<ReviewModel> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isPresent()) {
            ReviewModel reviewModel = optionalReview.get();

            CurtidaModel curtida = new CurtidaModel();
            curtida.setUsuario(curtidaDto.getUsuario());
            curtida.setReviewId(reviewModel.getId());

            return curtidaRepository.save(curtida);
        }
        return null;
    }

    public void deleteCurtida(Long id) {
        curtidaRepository.deleteById(id);
    }
}
