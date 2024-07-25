package cs2024.inf.CineView.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
public class ReviewModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private Long idFilme;

    @OneToMany(mappedBy = "reviewId", cascade = CascadeType.ALL)
    private List<CurtidaModel> curtidas;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<CurtidaModel> getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(List<CurtidaModel> curtidas) {
        this.curtidas = curtidas;
    }
}
