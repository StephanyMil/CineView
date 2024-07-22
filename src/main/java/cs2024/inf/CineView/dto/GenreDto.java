package cs2024.inf.CineView.dto;

import jakarta.validation.constraints.NotNull;

public record GenreDto(@NotNull Long id, @NotNull String name) {
}
