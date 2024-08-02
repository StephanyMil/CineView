package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.dto.reports.ReportDto;
import cs2024.inf.CineView.services.ReportService;
import cs2024.inf.CineView.repository.UserRepository;
import cs2024.inf.CineView.models.UserModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepository;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/review/{reviewId}")
    public ResponseEntity<Object> reportReview(@PathVariable Long reviewId, @Valid @RequestBody ReportDto reportDto) {
        try {
            String loggedInUserEmail = getAuthenticatedUserEmail();
            Optional<UserModel> loggedInUserOptional = userRepository.findByEmail(loggedInUserEmail);

            if (loggedInUserOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized.");
            }

            UserModel loggedInUser = loggedInUserOptional.get();
            Object reportDtoSaved = reportService.saveReport(loggedInUser.getId(), reviewId, null, reportDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(reportDtoSaved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/comment/{commentId}")
    public ResponseEntity<Object> reportComment(@PathVariable UUID commentId, @Valid @RequestBody ReportDto reportDto) {
        try {
            String loggedInUserEmail = getAuthenticatedUserEmail();
            Optional<UserModel> loggedInUserOptional = userRepository.findByEmail(loggedInUserEmail);

            if (loggedInUserOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized.");
            }

            UserModel loggedInUser = loggedInUserOptional.get();
            Object reportDtoSaved = reportService.saveReport(loggedInUser.getId(), null, commentId, reportDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(reportDtoSaved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<GenericPageableList> listReports(@RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(reportService.listReports(PageRequest.of(page, size)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }
        return authentication.getName();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Failed to convert '%s' with value '%s'", ex.getName(), ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}