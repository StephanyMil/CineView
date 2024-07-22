package cs2024.inf.CineView.services.tmdbService;

import cs2024.inf.CineView.models.GenreModel;
import cs2024.inf.CineView.repository.GenreRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@EnableScheduling
public class TmdbServiceGenre {
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private GenreRepository genreRepository;

    public void insertMovieGenres() {
        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=d13942dd547cde524c1167f5f07b2890";
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
