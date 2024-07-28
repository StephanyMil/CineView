package cs2024.inf.CineView.repository;

import cs2024.inf.CineView.models.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<CommentModel, UUID> {

}