package cs2024.inf.CineView.models;

import cs2024.inf.CineView.dto.reports.ViolationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Entity
public class ReportModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ViolationType violationType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date reportDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private ReviewModel review;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentModel comment;
}
