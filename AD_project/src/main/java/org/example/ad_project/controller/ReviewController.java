package org.example.ad_project.controller;

import org.example.ad_project.entity.Game;
import org.example.ad_project.entity.Review;
import org.example.ad_project.entity.User;
import org.example.ad_project.repository.GameRepository;
import org.example.ad_project.repository.ReviewRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {

    private final GameRepository gameRepository;
    private final ReviewRepository reviewRepository;

    public ReviewController(GameRepository gameRepository, ReviewRepository reviewRepository) {
        this.gameRepository = gameRepository;
        this.reviewRepository = reviewRepository;
    }

    // 게임 상세 페이지 (후기 포함)
    @GetMapping("/games/{id}")
    public String showGameDetail(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game == null) {
            return "error/404"; // 커스텀 404 페이지 필요 시
        }

        List<Review> reviews = reviewRepository.findByGame_Id(id);
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        model.addAttribute("game", game);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewForm", new Review()); // 후기 입력 폼 바인딩
        model.addAttribute("averageRating", averageRating);

        return "games/detail";
    }

    // 후기 등록
    @PostMapping("/games/{id}/reviews")
    public String addReview(@PathVariable Long id,
                            @ModelAttribute Review review,
                            HttpSession session) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game == null) return "redirect:/";

        // 작성자 ID 자동 설정
        Object userObj = session.getAttribute("loggedInUser");
        if (userObj instanceof User user) {
            review.setAuthor(user.getUserid());
        } else {
            review.setAuthor("익명");
        }

        review.setId(null); // Insert로 인식되도록 명시
        review.setGame(game);
        reviewRepository.save(review);

        return "redirect:/games/" + id;
    }



    // 후기 수정 폼
    @GetMapping("/reviews/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return "redirect:/"; // 잘못된 접근 방지

        model.addAttribute("review", review);
        return "reviews/edit";
    }

    // 후기 업데이트 처리
    @PostMapping("/reviews/{id}/edit")
    public String updateReview(@PathVariable Long id, @ModelAttribute Review updatedReview) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return "redirect:/";

        review.setAuthor(updatedReview.getAuthor());  // 향후 로그인 기반으로 제한 가능
        review.setContent(updatedReview.getContent());
        review.setRating(updatedReview.getRating());

        reviewRepository.save(review);
        return "redirect:/games/" + review.getGame().getId();
    }

    // 후기 삭제 처리
    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return "redirect:/";

        Long gameId = review.getGame().getId();
        reviewRepository.deleteById(id);
        return "redirect:/games/" + gameId;
    }
}

