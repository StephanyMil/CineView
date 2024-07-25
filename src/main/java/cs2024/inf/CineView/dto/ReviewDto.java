package cs2024.inf.CineView.dto;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class ReviewDto {

    @NotNull
    private String usuario;

    @NotNull
    private Date dataAssistido;

    @NotNull
    private Integer nota;

    @NotNull
    private String comentario;

    @NotNull
    private Date dataReview;

    @NotNull
    private Long idFilme; // Adiciona esse campo

    // Getters and Setters

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getDataAssistido() {
        return dataAssistido;
    }

    public void setDataAssistido(Date dataAssistido) {
        this.dataAssistido = dataAssistido;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getDataReview() {
        return dataReview;
    }

    public void setDataReview(Date dataReview) {
        this.dataReview = dataReview;
    }

    public Long getIdFilme() {
        return idFilme;
    }

    public void setIdFilme(Long idFilme) {
        this.idFilme = idFilme;
    }
}
