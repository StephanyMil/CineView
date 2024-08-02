package cs2024.inf.CineView.services.reviewService;

import cs2024.inf.CineView.dto.GenericPageableList;
import cs2024.inf.CineView.dto.ReviewDto;
import cs2024.inf.CineView.handler.BusinessException;
import cs2024.inf.CineView.models.LikeModel;
import cs2024.inf.CineView.models.MovieModel;
import cs2024.inf.CineView.models.ReviewModel;
import cs2024.inf.CineView.models.UserModel;
import cs2024.inf.CineView.repository.LikeRepository;
import cs2024.inf.CineView.repository.MovieRepository;
import cs2024.inf.CineView.repository.ReviewRepository;
import cs2024.inf.CineView.repository.UserRepository;
import cs2024.inf.CineView.services.UserService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserService userService;

    public List<ReviewDto> findAll() {
        List<ReviewModel> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public Object findById(Long id) {
        try {
            if (!reviewRepository.existsById(id)) {
                throw new BusinessException("This review does not exist");
            }
            return convertToDto(reviewRepository.findById(id).get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }

    }

    @Transactional
    public List<ReviewDto> findByUser(Pageable pageable) {
        UserModel userModel = userService.getUserByAuth();
        Page<ReviewModel> userReviewList = reviewRepository.findByUserId(userModel.getId(), pageable);
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
    public void incrementLikes(Long reviewId) {
        UserModel userModel = userService.getUserByAuth();
        ReviewModel review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException("Review not found"));
        Optional<LikeModel> optionalLike = likeRepository.findByReviewIdAndUserId(reviewId, userModel.getId());
        if (!optionalLike.isPresent()) {

            LikeModel likeModel = new LikeModel();
            likeModel.setReview(review);
            likeModel.setLikeDate(new Date());
            likeModel.setUser(userModel);
            likeRepository.save(likeModel);

            review.setLikesQtd(review.getLikesQtd() + 1);
            reviewRepository.save(review);
        } else {
            throw new BusinessException("This user has already liked the review");
        }
    }

    @Transactional
    public ReviewModel decreaseLike(Long reviewId) {
        UserModel userModel = userService.getUserByAuth();
        Optional<ReviewModel> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent()) {
            ReviewModel review = optionalReview.get();

            Optional<LikeModel> optionalLike = likeRepository.findByReviewIdAndUserId(reviewId, userModel.getId());
            if (optionalLike.isPresent()) {
                likeRepository.delete(optionalLike.get());

                review.setLikesQtd(review.getLikesQtd() - 1);
                reviewRepository.save(review);

                return review;
            } else {
                throw new BusinessException("This user didn't liked the review");
            }
        } else {
            throw new BusinessException("Review not found");
        }
    }

    @Transactional
    public Object postReview(ReviewDto reviewDto) {
        String userEmail = userService.getAuthenticatedUserEmail();

        if (userEmail.equals("anonymousUser")) {
            throw new BusinessException("The login session has expired");
        }
        UserModel user = userRepository.findByEmail(userEmail).orElseThrow(() -> new BusinessException("User not found"));

        Long movieId = reviewDto.getMovie_id();
        MovieModel movie = movieRepository.findById(movieId).orElseThrow(() -> new BusinessException("Movie doen't exist in our database"));

        if (reviewDto.getRating() > 5 || reviewDto.getRating() < 1) {
            throw new BusinessException("The rating is invalid. It should be between 1 and 5");
        }
        if (reviewRepository.existsByUserIdAndMovieId(reviewDto.getUser_id(), movie.getId())) {
            throw new BusinessException("User has already reviewed this movie");
        }

        return this.saveReview(user, reviewDto, movie);

    }

    @Transactional
    public Object editReview(ReviewDto reviewDto) {
        UserModel user = userService.getUserByAuth();

        Long movieId = reviewDto.getMovie_id();
        MovieModel movie = movieRepository.findById(movieId).orElseThrow(() -> new BusinessException("Movie doen't exist in our database"));

        if (reviewDto.getRating() > 5 || reviewDto.getRating() < 1) {
            throw new BusinessException("It should be between 1 and 5");
        }
        return this.saveReview(user, reviewDto, movie);

    }

    @Transactional
    public Object saveReview(UserModel userModel, ReviewDto reviewDto, MovieModel movieModel) {
        try {
            ReviewModel reviewModel = new ReviewModel();
            BeanUtils.copyProperties(reviewDto, reviewModel);
            reviewModel.setUser(userModel);
            reviewModel.setMovie(movieModel);
            reviewModel.setPublicationDate(new Date());

            movieModel.setTotalReviewVotes(movieModel.getTotalReviewVotes() + reviewModel.getRating());
            movieModel.setVoteCount(movieModel.getVoteCount() + 1);
            movieModel.setVoteAverage(movieModel.getTotalReviewVotes() / movieModel.getVoteCount());
            movieRepository.save(movieModel);

            ReviewModel savedReview = reviewRepository.save(reviewModel);
            ReviewDto savedReviewDto = new ReviewDto();
            BeanUtils.copyProperties(savedReview, savedReviewDto);
            savedReviewDto.setUser_id(userModel.getId());
            savedReviewDto.setMovie_id(movieModel.getId());

            return savedReviewDto;

        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
