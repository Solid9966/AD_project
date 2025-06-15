package org.example.ad_project.controller;

import jakarta.servlet.http.HttpSession;
import org.example.ad_project.entity.Game;
import org.example.ad_project.entity.Review;
import org.example.ad_project.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 게임 상세 페이지 (후기 포함)
    @GetMapping("/games/{id}")
    public String showGameDetail(@PathVariable Long id, Model model) {
        Game game = reviewService.getGameById(id);
        if (game == null) {
            return "error/404";
        }

        List<Review> reviews = reviewService.getReviewsForGame(id);
        double averageRating = reviewService.calculateAverageRating(reviews);

        model.addAttribute("game", game);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewForm", new Review()); // 빈 폼 객체
        model.addAttribute("averageRating", averageRating);

        return "games/detail";
    }

    // 후기 등록 처리
    @PostMapping("/games/{id}/reviews")
    public String addReview(@PathVariable Long id,
                            @ModelAttribute Review review,
                            HttpSession session) {
        reviewService.saveReview(id, review, session);
        return "redirect:/games/" + id;
    }

    // 후기 수정 폼
    @GetMapping("/reviews/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Review review = reviewService.getReviewById(id);
        if (review == null) return "redirect:/";
        model.addAttribute("review", review);
        return "reviews/edit";
    }

    // 후기 업데이트 처리
    @PostMapping("/reviews/{id}/edit")
    public String updateReview(@PathVariable Long id,
                               @ModelAttribute Review updatedReview) {
        Review existingReview = reviewService.getReviewById(id);
        if (existingReview == null) return "redirect:/";

        updatedReview.setGame(existingReview.getGame()); // ⛏ 게임 정보 직접 지정
        reviewService.updateReview(id, updatedReview);

        return "redirect:/games/" + existingReview.getGame().getId(); // 안전하게 원본 참조
    }

    // 후기 삭제 처리
    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        if (review == null || review.getGame() == null) {
            return "redirect:/";
        }

        Long gameId = review.getGame().getId();
        reviewService.deleteReview(id);
        return "redirect:/games/" + gameId;
    }
}
