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

    @Transactional
    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody @Valid UserDto userDto) {
        try {
            // Verifica se o e-mail já está em uso
            Optional<UserModel> existingUser = userRepository.findByEmail(userDto.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já está em uso.");
            }

            var userModel = new UserModel();
            BeanUtils.copyProperties(userDto, userModel);
            userModel.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(userModel);

            return ResponseEntity.status(HttpStatus.CREATED).body(userModel);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a solicitação.");
        }
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

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> putUser(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserDto userDto) {
        try {
            Optional<UserModel> userFound = userRepository.findById(id);
            if (userFound.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user doesn't exist");
            }

            UserModel userModel = userFound.get();

            // Verifica se o e-mail está sendo alterado e se já está em uso por outro usuário
            if (!userModel.getEmail().equals(userDto.getEmail())) {
                Optional<UserModel> existingUserWithEmail = userRepository.findByEmail(userDto.getEmail());
                if (existingUserWithEmail.isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já está em uso.");
                }
            }

            // Copia as propriedades do DTO para o modelo, exceto o ID e a senha se não forem fornecidos
            userModel.setName(userDto.getName());
            userModel.setEmail(userDto.getEmail());
            userModel.setBirthDate(userDto.getBirthDate());
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                userModel.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }

            userRepository.save(userModel);

            // Popula o DTO para resposta
            UserDetailsDto userDetailsDto = new UserDetailsDto();
            userDetailsDto.setId(userModel.getId());
            userDetailsDto.setName(userModel.getName());
            userDetailsDto.setEmail(userModel.getEmail());
            userDetailsDto.setBirthDate(userModel.getBirthDate());
            userDetailsDto.setFollowers(userModel.getFollowers().stream().map(UserModel::getId).collect(Collectors.toSet()));
            userDetailsDto.setFollowing(userModel.getFollowing().stream().map(UserModel::getId).collect(Collectors.toSet()));

            // Verifica se deve incluir a senha na resposta (por razões de segurança, geralmente não se faz isso)
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                userDetailsDto.setPassword(userModel.getPassword()); // opcional, dependendo das políticas de segurança
            }

            return ResponseEntity.status(HttpStatus.OK).body(userDetailsDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a solicitação.");
        }
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

            // Adiciona o usuário seguido à lista de seguidos do usuário atual
            user.getFollowing().add(follow);
            // Adiciona o usuário atual à lista de seguidores do usuário seguido
            follow.getFollowers().add(user);

            // Salva ambas as entidades
            userRepository.save(user);
            userRepository.save(follow);

            return ResponseEntity.ok("Followed successfully");
        }

        return ResponseEntity.status(404).body("User not found");
    }

    @Transactional
    @PostMapping("/{userId}/unfollow/{followId}")
    public ResponseEntity<?> unfollowUser(@PathVariable UUID userId, @PathVariable UUID followId) {
        Optional<UserModel> userOptional = userRepository.findById(userId);
        Optional<UserModel> followOptional = userRepository.findById(followId);

        if (userOptional.isPresent() && followOptional.isPresent()) {
            UserModel user = userOptional.get();
            UserModel follow = followOptional.get();

            // Remove o usuário seguido da lista de seguidos do usuário atual
            user.getFollowing().remove(follow);
            // Remove o usuário atual da lista de seguidores do usuário seguido
            follow.getFollowers().remove(user);

            // Salva ambas as entidades
            userRepository.save(user);
            userRepository.save(follow);

            return ResponseEntity.ok("Unfollowed successfully");
        }

        return ResponseEntity.status(404).body("User not found");
    }
}
