package cs2024.inf.CineView.services;

import cs2024.inf.CineView.dto.reports.ReportDto;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.models.ReportModel;
import cs2024.inf.CineView.models.ReviewModel;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.ReportRepository;
import cs2024.inf.CineView.repository.ReviewRepository;
import cs2024.inf.CineView.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public Object saveReport(UUID userId, Long reviewId, ReportDto reportDto) {
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("User not found"));
        ReviewModel review = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessException("Review not found"));

        ReportModel reportModel = new ReportModel();
        BeanUtils.copyProperties(reportDto, reportModel);
        reportModel.setUser(user);
        reportModel.setReview(review);
        reportModel.setReportDate(new Date());

        ReportModel savedReport = reportRepository.save(reportModel);
        ReportDto savedReportDto = new ReportDto();
        BeanUtils.copyProperties(savedReport, savedReportDto);

        return savedReportDto;
    }
}
