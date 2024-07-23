package cs2024.inf.CineView.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MOVIE-DB")
@Getter
@Setter
public class MovieModel {

    @Id
    private Long id;
    @Column(length = 2000)
    private String title;

    @Column(length = 2000)
    private String overview;

    private Date releaseDate;
    private float voteAverage;
    private int runtime;

    @Column(length = 2000)
    private String tagline;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GenreModel> genreModels = new ArrayList<>();

}
