package org.example.ad_project.service;

import jakarta.servlet.http.HttpSession;
import org.example.ad_project.entity.Game;
import org.example.ad_project.entity.Review;
import org.example.ad_project.entity.User;
import org.example.ad_project.repository.GameRepository;
import org.example.ad_project.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final GameRepository gameRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(GameRepository gameRepository, ReviewRepository reviewRepository) {
        this.gameRepository = gameRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviewsForGame(Long gameId) {
        return reviewRepository.findByGame_Id(gameId);
    }

    public double calculateAverageRating(List<Review> reviews) {
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public void saveReview(Long gameId, Review review, HttpSession session) {
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) throw new IllegalArgumentException("게임을 찾을 수 없습니다.");

        Object userObj = session.getAttribute("loggedInUser");
        if (userObj instanceof User user) {
            review.setAuthor(user.getUserid());
        } else {
            review.setAuthor("익명");
        }

        review.setId(null);
        review.setGame(game);
        reviewRepository.save(review);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public void updateReview(Long id, Review updatedReview) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return;

        review.setAuthor(updatedReview.getAuthor());
        review.setContent(updatedReview.getContent());
        review.setRating(updatedReview.getRating());

        reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            reviewRepository.deleteById(id);
        }
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }
}
