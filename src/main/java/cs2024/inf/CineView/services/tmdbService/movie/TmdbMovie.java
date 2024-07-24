package cs2024.inf.CineView.services.tmdbService.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TmdbMovie {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("release_date")
    private String releaseDate;

//    @JsonProperty("vote_average")
//    private float voteAverage;

//    @JsonProperty("popularity")
//    private Double popularity;

    @JsonProperty("genre_ids")
    private List<Integer> genre_ids;

}

