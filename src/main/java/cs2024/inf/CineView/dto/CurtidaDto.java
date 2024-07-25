package cs2024.inf.CineView.dto;

import jakarta.validation.constraints.NotNull;

public class CurtidaDto {

    @NotNull
    private String usuario;

    // Getters and Setters
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
