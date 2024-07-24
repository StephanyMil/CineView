package cs2024.inf.CineView.services.tmdbService.genre;

import cs2024.inf.CineView.models.GenreModel;
import cs2024.inf.CineView.repository.GenreRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@EnableScheduling
public class TmdbServiceGenre {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${tmdb.api.key}")
    private String apiKey;
    @Value("${tmdb.api.url.genre}")
    private String apiUrl;

    @Autowired
    private GenreRepository genreRepository;

    public void insertMovieGenres() {
        String url = apiUrl + "?api_key=" + apiKey;
        TmdbResponseGenre response = restTemplate.getForObject(url, TmdbResponseGenre.class);
        if (response != null && response.getResults() != null) {
            for (TmdbGenre tmdbGenre : response.getResults()) {
                GenreModel genreModel = new GenreModel();

                BeanUtils.copyProperties(tmdbGenre, genreModel);

                genreRepository.save(genreModel);
            }
        }
    }

    @PostConstruct
    public void init() {
        insertMovieGenres();
    }
}
