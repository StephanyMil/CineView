package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
}

