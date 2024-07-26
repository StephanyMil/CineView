package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.LikeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeModel, Long> {
}
