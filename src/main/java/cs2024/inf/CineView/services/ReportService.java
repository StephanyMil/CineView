package cs2024.inf.CineView.services;

import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.dto.reports.ReportDto;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.models.ReportModel;
import cs2024.inf.CineView.models.CommentModel;
import cs2024.inf.CineView.models.ReviewModel;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.ReportRepository;
import cs2024.inf.CineView.repository.CommentRepository;
import cs2024.inf.CineView.repository.ReviewRepository;
import cs2024.inf.CineView.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public Object saveReport(UUID userId, Long reviewId, UUID commentId, ReportDto reportDto) {
        try {
            UserModel user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("User not found"));
            ReviewModel review = null;
            CommentModel comment = null;

            if (reviewId != null) {
                review = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessException("Review not found"));
            }

            if (commentId != null) {
                comment = commentRepository.findById(commentId).orElseThrow(() -> new BusinessException("Comment not found"));
            }

            ReportModel reportModel = new ReportModel();
            BeanUtils.copyProperties(reportDto, reportModel);
            reportModel.setUser(user);
            reportModel.setReview(review);
            reportModel.setComment(comment);
            reportModel.setReportDate(new Date());

            ReportModel savedReport = reportRepository.save(reportModel);
            ReportDto savedReportDto = new ReportDto();
            BeanUtils.copyProperties(savedReport, savedReportDto);

            return savedReportDto;
        } catch (Exception e) {
            throw new BusinessException("Error saving report: " + e.getMessage());
        }
    }

    public GenericPageableList listReports(Pageable pageable) {
        try {
            Page<ReportModel> reportPage = reportRepository.findAll(pageable);
            List<Object> reportDtos = reportPage.stream().map(this::convertToDTO).collect(Collectors.toList());
            return new GenericPageableList(reportDtos, pageable);
        } catch (Exception e) {
            throw new BusinessException("Error listing reports: " + e.getMessage());
        }
    }

    private ReportDto convertToDTO(ReportModel reportModel) {
        ReportDto reportDto = new ReportDto();
        BeanUtils.copyProperties(reportModel, reportDto);
        return reportDto;
    }
}