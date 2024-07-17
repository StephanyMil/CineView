package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.LoginDto;
import cs2024.inf.CineView.dto.UserDto;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody @Valid LoginDto loginDto) {
        Optional<UserModel> userOpt = userRepository.findByEmail(loginDto.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok("User signed-in successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserDto userDto) {
        // Check if the email already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email is already taken!");
        }

        UserModel user = new UserModel();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setBirthDate(userDto.getBirthDate());

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}
