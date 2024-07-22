package cs2024.inf.CineView.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public class ReviewDto {

    @NotNull
    private UUID idFilme;
    @NotBlank
    private String usuario;
    @NotNull
    private LocalDate dataAssistido;
    @NotNull
    private int nota;
    @NotBlank
    private String comentario;
    @NotNull
    private LocalDate dataReview;

    // Getters e Setters
    public UUID getIdFilme() {
        return idFilme;
    }

    public void setIdFilme(UUID idFilme) {
        this.idFilme = idFilme;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataAssistido() {
        return dataAssistido;
    }

    public void setDataAssistido(LocalDate dataAssistido) {
        this.dataAssistido = dataAssistido;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getDataReview() {
        return dataReview;
    }

    public void setDataReview(LocalDate dataReview) {
        this.dataReview = dataReview;
    }
}
