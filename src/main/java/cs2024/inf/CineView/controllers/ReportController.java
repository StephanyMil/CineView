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

    @PostMapping("/review/{userId}/{reviewId}")
    public ResponseEntity<Object> reportReview(@PathVariable UUID userId, @PathVariable Long reviewId, @Valid @RequestBody ReportDto reportDto) {
        String loggedInUserEmail = getAuthenticatedUserEmail();
        Optional<UserModel> loggedInUserOptional = userRepository.findByEmail(loggedInUserEmail);

        if (loggedInUserOptional.isEmpty() || !loggedInUserOptional.get().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized.");
        }

        Object reportDtoSaved = reportService.saveReport(userId, reviewId, null, reportDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportDtoSaved);
    }

    @PostMapping("/comment/{userId}/{commentId}")
    public ResponseEntity<Object> reportComment(@PathVariable UUID userId, @PathVariable UUID commentId, @Valid @RequestBody ReportDto reportDto) {
        String loggedInUserEmail = getAuthenticatedUserEmail();
        Optional<UserModel> loggedInUserOptional = userRepository.findByEmail(loggedInUserEmail);

        if (loggedInUserOptional.isEmpty() || !loggedInUserOptional.get().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized.");
        }

        Object reportDtoSaved = reportService.saveReport(userId, null, commentId, reportDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportDtoSaved);
    }

    @GetMapping("{userId}")
    public ResponseEntity<GenericPageableList> listReports(@RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(reportService.listReports(PageRequest.of(page, size)));
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }
        return authentication.getName();
    }
}