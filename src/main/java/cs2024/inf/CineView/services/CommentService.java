package cs2024.inf.CineView.services;

import cs2024.inf.CineView.dto.CommentDto;
import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.models.CommentModel;
import cs2024.inf.CineView.models.ReviewModel;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.CommentRepository;
import cs2024.inf.CineView.repository.ReviewRepository;
import cs2024.inf.CineView.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public CommentModel createCommentary(CommentDto commentDto) {
        CommentModel commentModel = new CommentModel();
        commentModel.setText(commentDto.getText());

        // Buscar usuário e review a partir dos IDs
        Optional<UserModel> user = userRepository.findById(commentDto.getUserId());
        Optional<ReviewModel> review = reviewRepository.findById(commentDto.getReviewId());

        if (user.isPresent() && review.isPresent()) {
            commentModel.setUser(user.get());
            commentModel.setReview(review.get());
            return commentRepository.save(commentModel);
        } else {
            throw new RuntimeException("Usuário ou review não encontrados.");
        }
    }

    public CommentModel getCommentaryById(UUID id) {
        Optional<CommentModel> commentModel = commentRepository.findById(id);
        return commentModel.orElse(null);
    }

    public CommentModel editCommentary(UUID id, CommentDto commentDto) {
        Optional<CommentModel> commentModelOptional = commentRepository.findById(id);
        if (commentModelOptional.isPresent()) {
            CommentModel commentModel = commentModelOptional.get();
            commentModel.setText(commentDto.getText());
            return commentRepository.save(commentModel);
        }
        return null;
    }

    public void deleteCommentary(UUID id) {
        commentRepository.deleteById(id);
    }

    public GenericPageableList listCommentaries(Pageable pageable) {
        Page<CommentModel> commentPage = commentRepository.findAll(pageable);
        List<Object> commentDtos = commentPage.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new GenericPageableList(commentDtos, pageable);
    }

    private CommentDto convertToDTO(CommentModel commentModel) {
        CommentDto commentDto = new CommentDto();
        BeanUtils.copyProperties(commentModel, commentDto);
        return commentDto;
    }
}