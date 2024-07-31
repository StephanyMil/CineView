package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.LikeModel;
import cs2024.inf.CineView.models.ReviewModel;
import cs2024.inf.CineView.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<LikeModel, Long> {
    Optional<LikeModel> findByReviewIdAndUserId(Long reviewId, UUID userId);

    @Query("select l.review from LikeModel l where l.user = :user order by l.likeDate desc")
    Set<ReviewModel> findRecentLikedMoviesReviewsbyUser(UserModel user);

}
