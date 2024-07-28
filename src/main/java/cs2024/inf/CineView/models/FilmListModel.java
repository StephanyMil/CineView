package cs2024.inf.CineView.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "film_lists")
public class FilmListModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "film_list_movies",
            joinColumns = @JoinColumn(name = "film_list_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private List<MovieModel> movies;
}
