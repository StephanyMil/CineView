package cs2024.inf.CineView.services;

import cs2024.inf.CineView.dto.FilmListDto;
import cs2024.inf.CineView.dto.movies.MovieDto;
import cs2024.inf.CineView.models.FilmListModel;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.FilmListRepository;
import cs2024.inf.CineView.repository.MovieRepository;
import cs2024.inf.CineView.repository.UserRepository;
import cs2024.inf.CineView.handler.BusinessException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FilmListService {

    @Autowired
    private FilmListRepository filmListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<FilmListDto> findAll() {
        List<FilmListModel> filmLists = filmListRepository.findAll();
        return filmLists.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<FilmListDto> findFilmListsByUserId(UUID userId) {
        List<FilmListModel> filmLists = filmListRepository.findByUserId(userId);
        return filmLists.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public FilmListDto findById(Long id) {
        Optional<FilmListModel> filmListOptional = filmListRepository.findById(id);
        if (filmListOptional.isPresent()) {
            return convertToDto(filmListOptional.get());
        }
        throw new BusinessException("Film list not found");
    }

    @Transactional
    public FilmListDto createFilmList(FilmListDto filmListDto) {
        UUID userId = filmListDto.getUserId();
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));

        FilmListModel filmListModel = new FilmListModel();
        BeanUtils.copyProperties(filmListDto, filmListModel);
        filmListModel.setUser(user);

        List<MovieModel> movies = filmListDto.getMovies().stream()
                .map(movieDto -> movieRepository.findById(movieDto.getId())
                        .orElseThrow(() -> new BusinessException("Movie not found")))
                .collect(Collectors.toList());

        filmListModel.setMovies(movies);

        FilmListModel savedFilmList = filmListRepository.save(filmListModel);
        return convertToDto(savedFilmList);
    }

    @Transactional
    public FilmListDto updateFilmList(Long id, FilmListDto filmListDto) {
        Optional<FilmListModel> filmListOptional = filmListRepository.findById(id);
        if (filmListOptional.isPresent()) {
            FilmListModel filmListModel = filmListOptional.get();
            BeanUtils.copyProperties(filmListDto, filmListModel, "id", "user");

            List<MovieModel> movies = filmListDto.getMovies().stream()
                    .map(movieDto -> movieRepository.findById(movieDto.getId())
                            .orElseThrow(() -> new BusinessException("Movie not found")))
                    .collect(Collectors.toList());

            filmListModel.setMovies(movies);

            FilmListModel updatedFilmList = filmListRepository.save(filmListModel);
            return convertToDto(updatedFilmList);
        }
        throw new BusinessException("Film list not found");
    }

    @Transactional
    public void deleteFilmList(Long id) {
        if (filmListRepository.existsById(id)) {
            filmListRepository.deleteById(id);
        } else {
            throw new BusinessException("Film list not found");
        }
    }

    private FilmListDto convertToDto(FilmListModel filmListModel) {
        FilmListDto filmListDto = new FilmListDto();
        BeanUtils.copyProperties(filmListModel, filmListDto);

        List<MovieDto> movieDtos = filmListModel.getMovies().stream()
                .map(this::convertToMovieDto)
                .collect(Collectors.toList());
        filmListDto.setMovies(movieDtos);

        // Adiciona o userId ao DTO, se o usuário estiver presente
        if (filmListModel.getUser() != null) {
            filmListDto.setUserId(filmListModel.getUser().getId());
        }

        return filmListDto;
    }

    private MovieDto convertToMovieDto(MovieModel movieModel) {
        MovieDto movieDto = new MovieDto();
        BeanUtils.copyProperties(movieModel, movieDto);
        return movieDto;
    }
}
