package cs2024.inf.CineView.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "GENRE-DB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenreModel {
    @Id
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "genreModels")
    private List<MovieModel> moviesList;


}
