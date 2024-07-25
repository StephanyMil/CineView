package cs2024.inf.CineView.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private String releaseDate;
    private Double voteAverage;
    private Long voteCount;

    private Double popularity;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "movie-genres", joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<GenreModel> genreModels = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "movie-keywords", joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id"))
    private Set<KeywordModel> keywords = new HashSet<>();

}
