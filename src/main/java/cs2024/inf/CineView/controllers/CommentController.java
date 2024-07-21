package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.CommentDto;
import cs2024.inf.CineView.models.CommentModel;
import cs2024.inf.CineView.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/commentaries")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDto> createCommentary(@RequestBody CommentDto commentDto) {
        CommentModel commentModel = commentService.createCommentary(commentDto);
        CommentDto commentDtoCreated = new CommentDto(commentModel);
        return new ResponseEntity<>(commentDtoCreated, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> listCommentaries() {
        List<CommentModel> commentaries = commentService.listCommentaries();
        List<CommentDto> commentDto = commentaries.stream()
                .map(CommentDto::new)
                .toList();
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentaryById(@PathVariable UUID id) { // Use UUID aqui
        CommentModel commentary = commentService.getCommentaryById(id);
        if (commentary == null) {
            return ResponseEntity.notFound().build();
        }
        CommentDto commentDto = new CommentDto(commentary);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> editCommentary(@PathVariable UUID id, @RequestBody CommentDto commentDto) { // Use UUID aqui
        CommentModel commentModel = commentService.editCommentary(id, commentDto);
        if (commentModel == null) {
            return ResponseEntity.notFound().build();
        }
        CommentDto commentDtoUpdated = new CommentDto(commentModel);
        return new ResponseEntity<>(commentDtoUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentary(@PathVariable UUID id) { // Use UUID aqui
        commentService.deleteCommentary(id);
        return ResponseEntity.noContent().build();
    }
}