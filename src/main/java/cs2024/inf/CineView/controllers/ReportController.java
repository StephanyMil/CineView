package cs2024.inf.CineView.controllers;

import cs2024.inf.CineView.dto.reports.ReportDto;
import cs2024.inf.CineView.services.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("reports")
public class ReportController {

    final
    ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/{userId}/{reviewId}")
    public ResponseEntity<Object> reportReview(@PathVariable UUID userId, @PathVariable Long reviewId, @Valid @RequestBody ReportDto reportDto) {
        Object reportDtoSaved = reportService.saveReport(userId, reviewId, reportDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportDtoSaved);
    }
}