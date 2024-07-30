package cs2024.inf.CineView.models;

import cs2024.inf.CineView.dto.reports.ViolationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "REPORTS")
@Getter
@Setter
public class ReportModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "review_id")
    private ReviewModel review;

    private String reason;

    @Enumerated(EnumType.STRING)
    private ViolationType violationType;

    private Date reportDate;
}