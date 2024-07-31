package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.FilmListModel;
import cs2024.inf.CineView.models.MovieModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface FilmListRepository extends JpaRepository<FilmListModel, Long> {
    List<FilmListModel> findByUserId(UUID userId);

    //filmes adicionados recentemente em listas do usuário
    @Query("SELECT DISTINCT m FROM FilmListModel l JOIN l.movies m LEFT JOIN FETCH m.keywords WHERE l.user.id = :userId")
    Set<MovieModel> findRecentByUserId(@Param("userId") UUID userId);


}
