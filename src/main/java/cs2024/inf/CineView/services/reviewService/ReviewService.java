package cs2024.inf.CineView.services.reviewService;

import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.dto.ReviewDto;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.models.ReviewModel;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.MovieRepository;
import cs2024.inf.CineView.repository.ReviewRepository;
import cs2024.inf.CineView.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<ReviewDto> findAll() {
        List<ReviewModel> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public Object findById(Long id) {
        if (!reviewRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This review does not exist");
        }
        return convertToDto(reviewRepository.findById(id).get());
    }

    @Transactional
    public List<ReviewDto> findByUser(UUID id, Pageable pageable) {
        Page<ReviewModel> userReviewList = reviewRepository.findByUserId(id, pageable);
        return userReviewList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public GenericPageableList findMovieReviews(Long id, Pageable pageable) {
        List<ReviewModel> movieReviewList = reviewRepository.findByMovieId(id);
        List<Object> movieReviewListDto = movieReviewList.stream().map(this::convertToDto).collect(Collectors.toList());
        return new GenericPageableList(movieReviewListDto, pageable);
    }

    public ReviewDto convertToDto(ReviewModel review) {
        ReviewDto reviewDto = new ReviewDto();
        BeanUtils.copyProperties(review, reviewDto);
        reviewDto.setUser_id(review.getUser().getId());
        reviewDto.setMovie_id(review.getMovie().getId());
        return reviewDto;
    }

    @Transactional
    public Object saveReview(ReviewDto reviewDto) {

        UUID userId = reviewDto.getUser_id();
        Long movieId = reviewDto.getMovie_id();
        UserModel user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("User not found"));
        MovieModel movie = movieRepository.findById(movieId).orElseThrow(() -> new BusinessException("Movie doen't exist in our database"));

        if (reviewDto.getRating() > 5 || reviewDto.getRating() < 1) {
            throw new BusinessException("The rating is invalid");

        }
        if (reviewRepository.existsByUserIdAndMovieId(reviewDto.getUser_id(), movie.getId())) {
            throw new BusinessException("User has already reviewed this movie");
        }
        try {
            ReviewModel reviewModel = new ReviewModel();
            BeanUtils.copyProperties(reviewDto, reviewModel);
            reviewModel.setUser(user);
            reviewModel.setMovie(movie);
            reviewModel.setPublicationDate(new Date());

            ReviewModel savedReview = reviewRepository.save(reviewModel);
            ReviewDto savedReviewDto = new ReviewDto();
            BeanUtils.copyProperties(savedReview, savedReviewDto);
            savedReviewDto.setUser_id(userId);
            savedReviewDto.setMovie_id(movieId);

            return savedReviewDto;

        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
