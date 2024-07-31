package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.MovieModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<MovieModel, Long> {
    @Query("SELECT m FROM MovieModel m JOIN m.keywords k on k.name IN :keywords")
    List<MovieModel> findByKeywordsIn(Set<String> keywords, Pageable pageable);


    @Query("SELECT m FROM MovieModel m " +
            "JOIN m.keywords k " +
            "WHERE k.id IN :keywordIds " +
            "GROUP BY m.id")
    Page<MovieModel> findMoviesByCommonKeywords(@Param("keywordIds") Set<Long> keywordIds, Pageable pageable);
}
