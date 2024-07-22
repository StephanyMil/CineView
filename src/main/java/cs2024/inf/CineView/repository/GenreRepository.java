package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.GenreModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreModel, Integer> {
}

