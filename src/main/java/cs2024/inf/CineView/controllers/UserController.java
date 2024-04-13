package cs2024.inf.CineView.controllers;
import cs2024.inf.CineView.dto.UserDto;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserDto userDto) {
        var userModel = new UserModel();

        //converte os dados para a classe modelo
        BeanUtils.copyProperties(userDto, userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(userModel));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "id") UUID id) {

        Optional<UserModel> userFound = userRepository.findById(id);

        if (userFound.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user doesn't exist");
        }

        return ResponseEntity.status(HttpStatus.OK).body(userFound.get());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Object> putUser(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserDto userDto) {
        Optional<UserModel> userFound = userRepository.findById(id);

        if (userFound.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user doesn't exist");
        }

        var userModel = userFound.get();
        BeanUtils.copyProperties(userDto, userModel);

        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id){
        Optional<UserModel> userFound = userRepository.findById(id);

        if(userFound.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user doesn't exist");
        }
        userRepository.delete(userFound.get());
        return ResponseEntity.status(HttpStatus.OK).body("User was deleted successfully");
    }
}