package cs2024.inf.CineView.repositories;

import cs2024.inf.CineView.models.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewModel, Long> {
    List<ReviewModel> findByMovieId(Long movieId);
}
