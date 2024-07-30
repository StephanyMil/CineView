package cs2024.inf.CineView.dto.reports;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReportDto {
    @NotBlank(message = "The reason can't be blank")
    private String reason;

    @NotNull(message = "The violation type can't be null")
    private ViolationType violationType;

    private Long reviewId; // Id da review (opcional)
    private UUID commentId; // Id do comentário (opcional)
}
