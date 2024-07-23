package cs2024.inf.CineView.services.tmdbService;

import cs2024.inf.CineView.models.GenreModel;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.repository.GenreRepository;
import cs2024.inf.CineView.repository.MovieRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@EnableScheduling
public class TmdbServiceMovie {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${tmdb.api.key}")
    private String apiKey;
    @Value("${tmdb.api.url.latest.movies}")
    private String apiUrl;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private GenreRepository genreRepository;


    @Transactional
    public void updateNowPlayingMovies() {
        String url = apiUrl + "?api_key=" + apiKey;
        TmdbResponseMovie response = restTemplate.getForObject(url, TmdbResponseMovie.class);
        if (response != null && response.getResults() != null) {
            for (TmdbMovie tmdbMovie : response.getResults()) {
                MovieModel movieModel = new MovieModel();
                BeanUtils.copyProperties(tmdbMovie, movieModel);

                List<GenreModel> genreModels = genreRepository.findAllById(tmdbMovie.getGenre_ids());

                movieModel.setGenreModels(genreModels);

                movieRepository.save(movieModel);
            }
        }
    }

    @PostConstruct
    public void init() {
        updateNowPlayingMovies();
    }


    @Scheduled(cron = "0 0 0 * * SUN") // A cada domingo à meia-noite
    public void scheduleWeeklyUpdate() {
        updateNowPlayingMovies();
    }

}
