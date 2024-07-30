package cs2024.inf.CineView.services.csvImportService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import cs2024.inf.CineView.models.GenreModel;
import cs2024.inf.CineView.models.KeywordModel;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.repository.GenreRepository;
import cs2024.inf.CineView.repository.KeywordRepository;
import cs2024.inf.CineView.repository.MovieRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class CsvImportService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private GenreRepository genreRepository;

    @Transactional
    @PostConstruct
    public void importMoviesFromCsv() {
        if (movieRepository.count() > 0 || keywordRepository.count() > 0 || genreRepository.count() > 0) {
            return;
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/data/tmdb_5000_movies.csv"))))) {
            reader.readNext(); // Pula a leitura do cabeçalho
            String[] nextLine;

            List<GenreModel> allGenres = new ArrayList<>();
            Set<KeywordModel> allKeywords = new HashSet<>();
            List<MovieModel> moviesToSave = new ArrayList<>();

            while ((nextLine = reader.readNext()) != null) {
                Long id = parseLongSafely(nextLine[0]);
                String title = nextLine[1];
                String overview = nextLine[2];
                String keywordsJson = nextLine[3].replace("\"\"", "\\\"");
                String releaseDate = nextLine[4];
                String genreJson = nextLine[8].replace("\"\"", "\\\"");

                List<GenreModel> genres = jacksonObjectMapper.readValue(genreJson, new TypeReference<List<GenreModel>>() {
                });
                allGenres.addAll(genres);

                Set<KeywordModel> keywords = jacksonObjectMapper.readValue(keywordsJson, new TypeReference<Set<KeywordModel>>() {
                });
                allKeywords.addAll(keywords);

                if (!movieRepository.existsById(id)) {
                    MovieModel movie = new MovieModel();
                    movie.setId(id);
                    movie.setTitle(title);
                    movie.setOverview(overview);
                    movie.setReleaseDate(releaseDate);
                    movie.setGenreModels(genres);
                    movie.setKeywords(keywords);

                    moviesToSave.add(movie);
                }
            }

            genreRepository.saveAll(allGenres);
            keywordRepository.saveAll(allKeywords);
            movieRepository.saveAll(moviesToSave);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private Long parseLongSafely(String value) {
        try {
            return Long.parseLong(value.replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter para int: " + value);
            return 0L;
        }
    }

}
