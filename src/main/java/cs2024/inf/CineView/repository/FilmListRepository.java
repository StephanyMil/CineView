package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.FilmListModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmListRepository extends JpaRepository<FilmListModel, Long> {
}
