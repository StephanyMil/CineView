package cs2024.inf.CineView.services.tmdbService.movie;

import cs2024.inf.CineView.models.GenreModel;
import cs2024.inf.CineView.models.KeywordModel;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.repository.GenreRepository;
import cs2024.inf.CineView.repository.KeywordRepository;
import cs2024.inf.CineView.repository.MovieRepository;
import cs2024.inf.CineView.services.tmdbService.keyword.TmdbKeyword;
import cs2024.inf.CineView.services.tmdbService.keyword.TmdbResponseKeyword;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@EnableScheduling
public class TmdbServiceMovie {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.url.movie}")
    private String apiUrlMovie;

    @Value("${tmdb.api.url.movie.keywods}")
    private String apiUrlMovieKeywods;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private KeywordRepository keywordRepository;


    @Transactional
    public void updateNowPlayingMovies() {
        String url = apiUrlMovie + "?api_key=" + apiKey;
        TmdbResponseMovie response = restTemplate.getForObject(url, TmdbResponseMovie.class);
        if (response != null && response.getResults() != null) {
            for (TmdbMovie tmdbMovie : response.getResults()) {
                MovieModel movieModel = new MovieModel();

                List<GenreModel> genres = genreRepository.findAllById(tmdbMovie.getGenre_ids());
                movieModel.setGenreModels(genres);

                BeanUtils.copyProperties(tmdbMovie, movieModel);
                Set<KeywordModel> keys = getMovieKeyword(tmdbMovie.getId());
                movieModel.setKeywords(keys);

                movieRepository.save(movieModel);
            }
        }
    }

    @Transactional
    public Set<KeywordModel> getMovieKeyword(Long id) {
        String url = apiUrlMovieKeywods + id + "/keywords" + "?api_key=" + apiKey;
        TmdbResponseKeyword response = restTemplate.getForObject(url, TmdbResponseKeyword.class);
        Set<KeywordModel> keywordsModel = new HashSet<>();
        if (response != null && response.getKeywords() != null) {

            for (TmdbKeyword tmdbKeyword : response.getKeywords()) {

                KeywordModel keyword = new KeywordModel();
                BeanUtils.copyProperties(tmdbKeyword, keyword);

                keywordRepository.save(keyword);

                keywordsModel.add(keyword);

            }

            return keywordsModel;
        }

        return null;
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
