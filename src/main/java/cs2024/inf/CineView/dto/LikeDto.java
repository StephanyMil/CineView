package cs2024.inf.CineView.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeDto {
    Long id;
    @NotNull
    long user_id;
    @NotNull
    long review_id;
}
