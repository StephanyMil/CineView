package cs2024.inf.CineView.services.tmdbService.keyword;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TmdbKeyword {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;


}