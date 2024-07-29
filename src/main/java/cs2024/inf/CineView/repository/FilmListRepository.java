package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.FilmListModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface FilmListRepository extends JpaRepository<FilmListModel, Long> {
    List<FilmListModel> findByUserId(UUID userId);
}
