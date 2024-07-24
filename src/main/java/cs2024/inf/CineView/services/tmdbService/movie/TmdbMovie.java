package cs2024.inf.CineView.services.tmdbService.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
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
    private Date releaseDate;

    @JsonProperty("vote_average")
    private float voteAverage;

    @JsonProperty("runtime")
    private int runtime;

    @JsonProperty("tagline")
    private String tagline;

    @JsonProperty("genre_ids")
    private List<Integer> genre_ids;

    public List<Integer> getGenres_ids() {
        return genre_ids;
    }
}

