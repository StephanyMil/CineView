package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewModel, Long> {
    List<ReviewModel> findByIdFilme(Long idFilme);
}
