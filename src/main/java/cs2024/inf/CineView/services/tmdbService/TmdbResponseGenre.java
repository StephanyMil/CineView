package cs2024.inf.CineView.services.tmdbService;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbResponseGenre {

    @JsonProperty("genres")
    private List<TmdbGenre> genres;

    // Getters e Setters
    public List<TmdbGenre> getResults() {
        return genres;
    }

    public void setResults(List<TmdbGenre> results) {
        this.genres = results;
    }
}
