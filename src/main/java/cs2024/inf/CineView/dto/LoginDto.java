package cs2024.inf.CineView.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
