package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.FilmListDto;
import cs2024.inf.CineView.dto.UserDetailsDto;
import cs2024.inf.CineView.dto.UserDto;
import cs2024.inf.CineView.dto.movies.MovieDto;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.models.FilmListModel;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.FilmListRepository;
import cs2024.inf.CineView.repository.MovieRepository;
import cs2024.inf.CineView.repository.UserRepository;
import cs2024.inf.CineView.services.FilmListService;
import cs2024.inf.CineView.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FilmListRepository filmListRepository;

    @Autowired
    private FilmListService filmListService;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional
    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody @Valid UserDto userDto) {
        try {
            Optional<UserModel> existingUser = userRepository.findByEmail(userDto.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use.");
            }

            UserModel userModel = new UserModel();
            BeanUtils.copyProperties(userDto, userModel);
            userModel.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(userModel);

            return ResponseEntity.status(HttpStatus.CREATED).body(userModel);

        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while saving user", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data integrity violation.");
        } catch (Exception e) {
            logger.error("Error saving user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request.");
        }
    }

    @Transactional
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserModel> users = userRepository.findAll();
            List<UserDetailsDto> userDtos = users.stream().map(user -> {
                UserDetailsDto userDto = new UserDetailsDto();
                userDto.setId(user.getId());
                userDto.setName(user.getName());
                userDto.setEmail(user.getEmail());
                userDto.setPassword(user.getPassword());
                userDto.setBirthDate(user.getBirthDate());
                userDto.setFollowers(user.getFollowers().stream().map(UserModel::getId).collect(Collectors.toSet()));
                userDto.setFollowing(user.getFollowing().stream().map(UserModel::getId).collect(Collectors.toSet()));

                List<FilmListDto> filmListDtos = filmListService.findFilmListsByUserId(user.getId());

                userDto.setFilmLists(filmListDtos);

                return userDto;
            }).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(userDtos);
        } catch (Exception e) {
            logger.error("Error fetching all users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching users.");
        }
    }


    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "id") UUID id) {
        try {
            Optional<UserModel> userFound = userRepository.findById(id);
            if (userFound.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not authorized.");
            }

            UserModel user = userFound.get();
            UserDetailsDto userDto = new UserDetailsDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setBirthDate(user.getBirthDate());
            userDto.setFollowers(user.getFollowers().stream().map(UserModel::getId).collect(Collectors.toSet()));
            userDto.setFollowing(user.getFollowing().stream().map(UserModel::getId).collect(Collectors.toSet()));

            List<FilmListDto> filmListDtos = filmListService.findFilmListsByUserId(user.getId());

            userDto.setFilmLists(filmListDtos);

            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } catch (Exception e) {
            logger.error("Error fetching user by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user.");
        }
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> putUser(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserDto userDto) {
        try {
            Optional<UserModel> userFound = userRepository.findById(id);
            if (userFound.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not authorized.");
            }

            UserModel userModel = userFound.get();

            if (!userModel.getEmail().equals(userDto.getEmail())) {
                Optional<UserModel> existingUserWithEmail = userRepository.findByEmail(userDto.getEmail());
                if (existingUserWithEmail.isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use.");
                }
            }

            userModel.setName(userDto.getName());
            userModel.setEmail(userDto.getEmail());
            userModel.setBirthDate(userDto.getBirthDate());
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                userModel.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }

            userRepository.save(userModel);

            UserDetailsDto userDetailsDto = new UserDetailsDto();
            userDetailsDto.setId(userModel.getId());
            userDetailsDto.setName(userModel.getName());
            userDetailsDto.setEmail(userModel.getEmail());
            userDetailsDto.setBirthDate(userModel.getBirthDate());
            userDetailsDto.setFollowers(userModel.getFollowers().stream().map(UserModel::getId).collect(Collectors.toSet()));
            userDetailsDto.setFollowing(userModel.getFollowing().stream().map(UserModel::getId).collect(Collectors.toSet()));

            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                userDetailsDto.setPassword(userModel.getPassword());
            }

            return ResponseEntity.status(HttpStatus.OK).body(userDetailsDto);

        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while updating user", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data integrity violation.");
        } catch (Exception e) {
            logger.error("Error updating user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id) {
        try {
            Optional<UserModel> userFound = userRepository.findById(id);
            if (userFound.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not authorized.");
            }
            userRepository.delete(userFound.get());
            return ResponseEntity.status(HttpStatus.OK).body("User successfully deleted");
        } catch (Exception e) {
            logger.error("Error deleting user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request.");
        }
    }

    @Transactional
    @PostMapping("/{userId}/follow/{followId}")
    public ResponseEntity<?> followUser(@PathVariable UUID userId, @PathVariable UUID followId) {
        try {
            String loggedInUserEmail = getAuthenticatedUserEmail();
            Optional<UserModel> loggedInUserOptional = userRepository.findByEmail(loggedInUserEmail);

            // Check if the logged-in user is the same as the userId in the request
            if (loggedInUserOptional.isEmpty() || !loggedInUserOptional.get().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized.");
            }

            Optional<UserModel> userOptional = userRepository.findById(userId);
            Optional<UserModel> followOptional = userRepository.findById(followId);

            if (userOptional.isPresent() && followOptional.isPresent()) {
                UserModel user = userOptional.get();
                UserModel follow = followOptional.get();

                user.getFollowing().add(follow);
                follow.getFollowers().add(user);

                userRepository.save(user);
                userRepository.save(follow);

                return ResponseEntity.ok("Successfully followed");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            logger.error("Error following user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request.");
        }
    }

    @Transactional
    @PostMapping("/{userId}/unfollow/{followId}")
    public ResponseEntity<?> unfollowUser(@PathVariable UUID userId, @PathVariable UUID followId) {
        try {
            String loggedInUserEmail = getAuthenticatedUserEmail();
            Optional<UserModel> loggedInUserOptional = userRepository.findByEmail(loggedInUserEmail);

            // Check if the logged-in user is the same as the userId in the request
            if (loggedInUserOptional.isEmpty() || !loggedInUserOptional.get().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized.");
            }

            Optional<UserModel> userOptional = userRepository.findById(userId);
            Optional<UserModel> followOptional = userRepository.findById(followId);

            if (userOptional.isPresent() && followOptional.isPresent()) {
                UserModel user = userOptional.get();
                UserModel follow = followOptional.get();

                user.getFollowing().remove(follow);
                follow.getFollowers().remove(user);

                userRepository.save(user);
                userRepository.save(follow);

                return ResponseEntity.ok("Successfully unfollowed");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            logger.error("Error unfollowing user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request.");
        }
    }

    @Transactional
    @GetMapping("/profile")
    public ResponseEntity<Object> getUserProfile() {
        try {
            String userEmail = getAuthenticatedUserEmail();
            Optional<UserModel> userFound = userRepository.findByEmail(userEmail);
            if (userFound.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user does not exist");
            }

            UserModel user = userFound.get();
            UserDetailsDto userDto = new UserDetailsDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setBirthDate(user.getBirthDate());
            userDto.setFollowers(user.getFollowers().stream().map(UserModel::getId).collect(Collectors.toSet()));
            userDto.setFollowing(user.getFollowing().stream().map(UserModel::getId).collect(Collectors.toSet()));

            List<FilmListDto> filmListDtos = filmListService.findFilmListsByUserId(user.getId());

            userDto.setFilmLists(filmListDtos);

            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } catch (Exception e) {
            logger.error("Error fetching user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request.");
        }
    }

    @Transactional
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody @Valid UserDto userDto) {
        try {
            String userEmail = getAuthenticatedUserEmail();
            Optional<UserModel> userFound = userRepository.findByEmail(userEmail);
            if (userFound.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user does not exist");
            }

            UserModel userModel = userFound.get();

            if (!userModel.getEmail().equals(userDto.getEmail())) {
                Optional<UserModel> existingUserWithEmail = userRepository.findByEmail(userDto.getEmail());
                if (existingUserWithEmail.isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use.");
                }
            }

            userModel.setName(userDto.getName());
            userModel.setEmail(userDto.getEmail());
            userModel.setBirthDate(userDto.getBirthDate());
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                userModel.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }

            userRepository.save(userModel);

            UserDetailsDto userDetailsDto = new UserDetailsDto();
            userDetailsDto.setId(userModel.getId());
            userDetailsDto.setName(userModel.getName());
            userDetailsDto.setEmail(userModel.getEmail());
            userDetailsDto.setBirthDate(userModel.getBirthDate());
            userDetailsDto.setFollowers(userModel.getFollowers().stream().map(UserModel::getId).collect(Collectors.toSet()));
            userDetailsDto.setFollowing(userModel.getFollowing().stream().map(UserModel::getId).collect(Collectors.toSet()));

            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                userDetailsDto.setPassword(userModel.getPassword());
            }

            return ResponseEntity.status(HttpStatus.OK).body(userDetailsDto);

        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while updating user profile", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data integrity violation.");
        } catch (Exception e) {
            logger.error("Error updating user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request.");
        }
    }

    @Transactional
    @DeleteMapping("/profile")
    public ResponseEntity<Object> deleteUserProfile() {
        try {
            String userEmail = getAuthenticatedUserEmail();
            Optional<UserModel> userFound = userRepository.findByEmail(userEmail);
            if (userFound.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user does not exist");
            }
            userRepository.delete(userFound.get());
            return ResponseEntity.status(HttpStatus.OK).body("User successfully deleted");
        } catch (Exception e) {
            logger.error("Error deleting user profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request.");
        }
    }

    @Transactional
    @PostMapping("/{userId}/favorites/{filmListId}")
    public ResponseEntity<String> addFavoriteFilmList(@PathVariable UUID userId, @PathVariable Long filmListId) {
        try {
            String loggedInUserEmail = getAuthenticatedUserEmail();
            Optional<UserModel> loggedInUserOptional = userRepository.findByEmail(loggedInUserEmail);

            if (loggedInUserOptional.isEmpty() || !loggedInUserOptional.get().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized.");
            }

            filmListService.favoriteFilmList(userId, filmListId);
            return ResponseEntity.status(HttpStatus.OK).body("Film list added to favorites.");
        } catch (BusinessException e) {
            logger.error("Error adding film list to favorites", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Film list not found.");
        } catch (Exception e) {
            logger.error("Error processing request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request.");
        }
    }

    @Transactional
    @DeleteMapping("/{userId}/favorites/{filmListId}")
    public ResponseEntity<String> removeFavoriteFilmList(@PathVariable UUID userId, @PathVariable Long filmListId) {
        try {
            String loggedInUserEmail = getAuthenticatedUserEmail();
            Optional<UserModel> loggedInUserOptional = userRepository.findByEmail(loggedInUserEmail);

            if (loggedInUserOptional.isEmpty() || !loggedInUserOptional.get().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized.");
            }

            filmListService.unfavoriteFilmList(userId, filmListId);
            return ResponseEntity.status(HttpStatus.OK).body("Film list removed from favorites.");
        } catch (BusinessException e) {
            logger.error("Error removing film list from favorites", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Film list not found.");
        } catch (Exception e) {
            logger.error("Error processing request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request.");
        }
    }

    @Transactional
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<?> getFavoriteFilmLists(@PathVariable UUID userId) {
        try {
            String loggedInUserEmail = getAuthenticatedUserEmail();
            Optional<UserModel> loggedInUserOptional = userRepository.findByEmail(loggedInUserEmail);

            if (loggedInUserOptional.isEmpty() || !loggedInUserOptional.get().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized.");
            }

            List<FilmListDto> favoriteFilmLists = filmListService.findFavoriteFilmListsByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(favoriteFilmLists);
        } catch (BusinessException e) {
            logger.error("Error fetching user's favorite film lists", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Film list or user not found.");
        } catch (Exception e) {
            logger.error("Error processing request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request.");
        }
    }

    public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }
        return authentication.getName();
    }

    // Helper method to convert FilmListModel to FilmListDto
    private FilmListDto convertToFilmListDto(FilmListModel filmListModel) {
        FilmListDto filmListDto = new FilmListDto();
        BeanUtils.copyProperties(filmListModel, filmListDto);

        List<MovieDto> movieDtos = filmListModel.getMovies().stream()
                .map(this::convertToMovieDto)
                .collect(Collectors.toList());
        filmListDto.setMovies(movieDtos);

        return filmListDto;
    }

    // Helper method to convert MovieModel to MovieDto
    private MovieDto convertToMovieDto(MovieModel movieModel) {
        MovieDto movieDto = new MovieDto();
        BeanUtils.copyProperties(movieModel, movieDto);
        return movieDto;
    }
}
