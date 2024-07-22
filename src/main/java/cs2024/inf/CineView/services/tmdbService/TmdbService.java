package cs2024.inf.CineView.services.tmdbService;

import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.repository.GenreRepository;
import cs2024.inf.CineView.repository.MovieRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@EnableScheduling
public class TmdbService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${tmdb.api.key}")
    private String apiKey;
    @Value("${tmdb.api.url}")
    private String apiUrl;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private GenreRepository genreRepository;


    public void updateNowPlayingMovies() {
//        String url = apiUrl + "?api_key=" + apiKey;
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=d13942dd547cde524c1167f5f07b2890";
        TmdbResponse response = restTemplate.getForObject(url, TmdbResponse.class);
        if (response != null && response.getResults() != null) {
            for (TmdbMovie tmdbMovie : response.getResults()) {
                MovieModel movieModel = new MovieModel();

////                List<GenreModel> genres = genreRepository.findAllByd(tmdbMovie.getGenres_ids());
//                movieModel.setGenreModels(genres);

                BeanUtils.copyProperties(tmdbMovie, movieModel);

                movieRepository.save(movieModel);
            }
        }
    }

    @PostConstruct
    public void init() {
        updateNowPlayingMovies();
    }


//    public void insertMovieGenres() {
//        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=d13942dd547cde524c1167f5f07b2890";
//        TmdbResponseGenre response = restTemplate.getForObject(url, TmdbResponseGenre.class);
//        if (response != null && response.getResults() != null) {
//            for (TmdbGenre tmdbGenre : response.getResults()) {
//                GenreModel genreModel = new GenreModel();
//
//                BeanUtils.copyProperties(tmdbGenre, genreModel);
//
//                genreRepository.save(genreModel);
//            }
//        }
//    }

    @Scheduled(cron = "0 0 0 * * SUN") // A cada domingo à meia-noite
    public void scheduleWeeklyUpdate() {
        updateNowPlayingMovies();
    }

}
