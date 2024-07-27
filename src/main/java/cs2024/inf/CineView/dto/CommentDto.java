package cs2024.inf.CineView.dto;

import cs2024.inf.CineView.models.CommentModel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CommentDto {
    private UUID id; // Id do comentário
    private String text;
    private UUID userId; // Id do usuário que fez o comentário
    private UUID reviewId; // Id da review a qual o comentário pertence

    public CommentDto(CommentModel commentModel) {
        this.id = commentModel.getId();
        this.text = commentModel.getText();
        this.userId = commentModel.getUser().getId();
        // this.reviewId = commentModel.getReview().getId();
    }
}