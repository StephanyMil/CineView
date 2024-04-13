package cs2024.inf.CineView.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record UserDto(@NotNull String name, @NotNull String email, @NotNull String password, @NotNull Date birthDate) {
}
