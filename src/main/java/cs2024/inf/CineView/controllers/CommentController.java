package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.CommentDto;
import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.handler.NotFoundException;
import cs2024.inf.CineView.handler.UnauthorizedException;
import cs2024.inf.CineView.models.CommentModel;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.UserRepository;
import cs2024.inf.CineView.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/commentaries")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<?> createCommentary(@RequestBody CommentDto commentDto) {
        String userEmail = getAuthenticatedUserEmail();
        Optional<UserModel> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new UnauthorizedException("User not authorized.");
        }
        commentDto.setUserId(userOptional.get().getId());
        CommentModel commentModel = commentService.createCommentary(commentDto);
        CommentDto commentDtoCreated = new CommentDto(commentModel);
        return new ResponseEntity<>(commentDtoCreated, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<GenericPageableList> listCommentaries(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.listCommentaries(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentaryById(@PathVariable UUID id) {
        CommentModel commentary = commentService.getCommentaryById(id);
        if (commentary == null) {
            throw new NotFoundException("Comment not found.");
        }
        CommentDto commentDto = new CommentDto(commentary);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCommentary(@PathVariable UUID id, @RequestBody CommentDto commentDto) {
        String userEmail = getAuthenticatedUserEmail();
        Optional<UserModel> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new UnauthorizedException("User not authorized.");
        }
        CommentModel existingComment = commentService.getCommentaryById(id);
        if (existingComment == null || !existingComment.getUser().getId().equals(userOptional.get().getId())) {
            throw new UnauthorizedException("User not authorized to edit this comment.");
        }
        commentDto.setUserId(userOptional.get().getId());
        CommentModel commentModel = commentService.editCommentary(id, commentDto);
        CommentDto commentDtoUpdated = new CommentDto(commentModel);
        return new ResponseEntity<>(commentDtoUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommentary(@PathVariable UUID id) {
        String userEmail = getAuthenticatedUserEmail();
        Optional<UserModel> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            throw new UnauthorizedException("User not authorized.");
        }
        CommentModel existingComment = commentService.getCommentaryById(id);
        if (existingComment == null || !existingComment.getUser().getId().equals(userOptional.get().getId())) {
            throw new UnauthorizedException("User not authorized to delete this comment.");
        }
        commentService.deleteCommentary(id);
        return ResponseEntity.noContent().build();
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }
        return authentication.getName();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}
