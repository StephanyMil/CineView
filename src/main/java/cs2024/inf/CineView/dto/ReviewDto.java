package cs2024.inf.CineView.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class ReviewDto {

    @NotNull(message = "Usuário não pode ser nulo")
    @Size(min = 1, max = 255, message = "Usuário deve ter entre 1 e 255 caracteres")
    private String usuario;

    @NotNull(message = "Data assistido não pode ser nula")
    private LocalDateTime dataAssistido;

    @NotNull(message = "Nota não pode ser nula")
    private Float nota;

    @NotNull(message = "Comentário não pode ser nulo")
    @Size(min = 1, max = 1000, message = "Comentário deve ter entre 1 e 1000 caracteres")
    private String comentario;

    @NotNull(message = "Data do review não pode ser nula")
    private LocalDateTime dataReview;

    // Getters and Setters

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataAssistido() {
        return dataAssistido;
    }

    public void setDataAssistido(LocalDateTime dataAssistido) {
        this.dataAssistido = dataAssistido;
    }

    public Float getNota() {
        return nota;
    }

    public void setNota(Float nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataReview() {
        return dataReview;
    }

    public void setDataReview(LocalDateTime dataReview) {
        this.dataReview = dataReview;
    }

    public ReviewDto(String usuario, LocalDateTime dataAssistido, Float nota, String comentario, LocalDateTime dataReview) {
        this.usuario = usuario;
        this.dataAssistido = dataAssistido;
        this.nota = nota;
        this.comentario = comentario;
        this.dataReview = dataReview;
    }
}
