package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.UserDto;
import cs2024.inf.CineView.dto.UserDetailsDto;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.UserRepository;
import cs2024.inf.CineView.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserDto userDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(userModel));
    }

    @Transactional
    @GetMapping
    public ResponseEntity<List<UserDetailsDto>> getAllUsers() {
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
            return userDto;
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(userDtos);
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "id") UUID id) {
        Optional<UserModel> userFound = userRepository.findById(id);
        if (userFound.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user doesn't exist");
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

        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> putUser(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserDto userDto) {
        Optional<UserModel> userFound = userRepository.findById(id);
        if (userFound.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user doesn't exist");
        }
        var userModel = userFound.get();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setPassword(passwordEncoder.encode(userDto.getPassword()));  // Ensure password is updated securely
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id) {
        Optional<UserModel> userFound = userRepository.findById(id);
        if (userFound.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user doesn't exist");
        }
        userRepository.delete(userFound.get());
        return ResponseEntity.status(HttpStatus.OK).body("User was deleted successfully");
    }

    @Transactional
    @PostMapping("/{userId}/follow/{followId}")
    public ResponseEntity<?> followUser(@PathVariable UUID userId, @PathVariable UUID followId) {
        Optional<UserModel> userOptional = userRepository.findById(userId);
        Optional<UserModel> followOptional = userRepository.findById(followId);

        if (userOptional.isPresent() && followOptional.isPresent()) {
            UserModel user = userOptional.get();
            UserModel follow = followOptional.get();
            user.getFollowing().add(follow);
            userRepository.save(user);
            return ResponseEntity.ok("Followed successfully");
        }

        return ResponseEntity.status(404).body("User not found");
    }

    @Transactional
    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable UUID userId) {
        Optional<UserModel> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            Set<UserModel> followers = user.getFollowers();
            return ResponseEntity.ok(followers);
        }

        return ResponseEntity.status(404).body("User not found");
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<?> getFollowing(@PathVariable UUID userId) {
        Optional<UserModel> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            Set<UserModel> following = user.getFollowing();
            return ResponseEntity.ok(following);
        }

        return ResponseEntity.status(404).body("User not found");
    }
}
