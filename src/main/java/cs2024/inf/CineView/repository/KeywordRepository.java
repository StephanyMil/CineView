package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.KeywordModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordModel, Long> {
}
