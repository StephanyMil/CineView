package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.GenreModel;
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

    List<MovieModel> findByTitleContainingIgnoreCase(String title);

    List<MovieModel> findMovieModelsByTitleContainingIgnoreCaseAndGenreModels(String title, GenreModel genre);

    List<MovieModel> findMovieModelsByGenreModels(GenreModel genre);

    @Query("SELECT m FROM MovieModel m " +
            "JOIN m.keywords k " +
            "WHERE k.id IN :keywordIds " +
            "GROUP BY m.id")
    Page<MovieModel> findMoviesByCommonKeywords(@Param("keywordIds") Set<Long> keywordIds, Pageable pageable);


    //filmes com mais reviews, ordenados por avaliação e quantidade de likes
    @Query("SELECT m FROM MovieModel m inner JOIN ReviewModel  r on r.movie = m GROUP BY m ORDER BY COUNT(r.id) DESC, m.voteAverage desc, SUM(r.likesQtd) DESC")
    List<MovieModel> findMostReviewedAndLikedMovies(Pageable pageable);


    @Query(value = "SELECT m.id, m.title, COUNT(flm.film_list_id) AS listCount, COUNT(uf.film_list_id) AS favoritesCount " +
            "FROM movie_db m " +
            "INNER JOIN film_list_movies flm ON m.id = flm.movie_id " +
            "LEFT JOIN film_lists fl ON fl.id = flm.film_list_id " +
            "LEFT JOIN user_favorites uf ON uf.film_list_id = fl.id " +
            "GROUP BY m.id, m.title " +
            "ORDER BY listCount DESC, favoritesCount DESC " +
            "LIMIT 10",
            nativeQuery = true)
    List<Object[]> findMostSavedInLists();

}
