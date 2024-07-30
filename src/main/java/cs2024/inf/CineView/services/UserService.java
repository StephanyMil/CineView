package cs2024.inf.CineView.services;

import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserModel registerUser(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserModel findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserModel save(UserModel user) {
        return userRepository.save(user);
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
