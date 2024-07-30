package cs2024.inf.CineView.dto;

import jakarta.validation.constraints.NotNull;

public record LikeDto(Long id, @NotNull long user_id, @NotNull long review_id) {
}
