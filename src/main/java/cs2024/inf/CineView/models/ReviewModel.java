package cs2024.inf.CineView.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;  // Adicionando a importação necessária

@Entity
@Table(name = "reviews")
public class ReviewModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID idFilme;  // Adicionando o campo idFilme
    private String usuario;
    private LocalDate dataAssistido;
    private int nota;
    private String comentario;
    private LocalDate dataReview;

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
