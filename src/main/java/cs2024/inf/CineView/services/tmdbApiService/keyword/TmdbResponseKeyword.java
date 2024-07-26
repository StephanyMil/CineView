package cs2024.inf.CineView.services.tmdbApiService.keyword;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TmdbResponseKeyword {

    @JsonProperty("keywords")
    private List<TmdbKeyword> keywords;

}