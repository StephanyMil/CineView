package cs2024.inf.CineView.services;

import cs2024.inf.CineView.dto.CommentDto;
import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.handler.NotFoundException;
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

        Optional<UserModel> user = userRepository.findById(commentDto.getUserId());
        Optional<ReviewModel> review = reviewRepository.findById(commentDto.getReviewId());

        if (user.isPresent() && review.isPresent()) {
            commentModel.setUser(user.get());
            commentModel.setReview(review.get());
            return commentRepository.save(commentModel);
        } else {
            throw new NotFoundException("User or review not found.");
        }
    }

    public CommentModel getCommentaryById(UUID id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found."));
    }

    public CommentModel editCommentary(UUID id, CommentDto commentDto) {
        CommentModel commentModel = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found."));
        commentModel.setText(commentDto.getText());
        return commentRepository.save(commentModel);
    }

    public void deleteCommentary(UUID id) {
        if (!commentRepository.existsById(id)) {
            throw new NotFoundException("Comment not found.");
        }
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
