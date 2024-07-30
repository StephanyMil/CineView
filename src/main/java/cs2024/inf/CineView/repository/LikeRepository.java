package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.LikeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<LikeModel, Long> {
    Optional<LikeModel> findByReviewIdAndUserId(Long reviewId, UUID userId);

}
