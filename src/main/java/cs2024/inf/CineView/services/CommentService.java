package cs2024.inf.CineView.services;

import cs2024.inf.CineView.dto.CommentDto;
import cs2024.inf.CineView.models.CommentModel;
import cs2024.inf.CineView.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentModel createCommentary(CommentDto commentDto) {
        CommentModel commentModel = new CommentModel();
        commentModel.setText(commentDto.getText());
        // Setar usuário e review
        return commentRepository.save(commentModel);
    }

    public CommentModel getCommentaryById(UUID id) {
        Optional<CommentModel> commentModel = commentRepository.findById(id);
        return commentModel.orElse(null);
    }

    public CommentModel editCommentary(UUID id, CommentDto commentDto) { // Use UUID aqui
        Optional<CommentModel> commentModelOptional = commentRepository.findById(id);
        if (commentModelOptional.isPresent()) {
            CommentModel commentModel = commentModelOptional.get();
            commentModel.setText(commentDto.getText());

            return commentRepository.save(commentModel);
        }
        return null;
    }

    public void deleteCommentary(UUID id) { // Use UUID aqui
        commentRepository.deleteById(id);
    }

    public List<CommentModel> listCommentaries() {
        return commentRepository.findAll();
    }
}