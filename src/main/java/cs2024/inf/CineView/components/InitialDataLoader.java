package cs2024.inf.CineView.components;

import cs2024.inf.CineView.repository.GenreRepository;
import cs2024.inf.CineView.repository.MovieRepository;
import cs2024.inf.CineView.services.tmdbService.genre.TmdbServiceGenre;
import cs2024.inf.CineView.services.tmdbService.movie.TmdbServiceMovie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private TmdbServiceGenre tmdbServiceGenre;

    @Override
    public void run(String... args) throws Exception {
        TmdbServiceMovie tmdbServiceMovie = new TmdbServiceMovie();
        if (genreRepository.count() == 0) {
            tmdbServiceGenre.insertMovieGenres();
        }
//        if (movieRepository.count() == 0) {
//            tmdbService.updateNowPlayingMovies();
//        }

    }
}
