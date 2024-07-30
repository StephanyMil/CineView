package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.MovieModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<MovieModel, Long> {
    List<MovieModel> findByTitleContainingIgnoreCase(String title);
}
