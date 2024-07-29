package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.ReviewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewModel, Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM ReviewModel r WHERE r.user.id = :userId AND r.movie.id = :movieId")
    boolean existsByUserIdAndMovieId(@Param("userId") UUID userId, @Param("movieId") Long movieId);

    Page<ReviewModel> findByUserId(UUID userId, Pageable pageable);

    //    @Query("SELECT r FROM ReviewModel r JOIN FETCH r.user JOIN FETCH r.movie WHERE r.movie.id = :movieId")
    List<ReviewModel> findByMovieId(Long movieId);

}