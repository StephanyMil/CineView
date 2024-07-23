package cs2024.inf.CineView.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class ReviewModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long movieId;

    @NotNull
    @Column(nullable = false, length = 255)
    private String usuario;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataAssistido;

    @NotNull
    @Column(nullable = false)
    private Float nota;

    @NotNull
    @Column(nullable = false, length = 1000)
    private String comentario;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataReview;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

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
}
