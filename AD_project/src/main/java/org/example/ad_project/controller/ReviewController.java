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

    @GetMapping("/games/{id}")
    public String showGameDetail(@PathVariable Long id, Model model) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game == null) {
            return "error/404";
        }

        List<Review> reviews = reviewRepository.findByGame_Id(id);
        double averageRating = reviews.isEmpty() ? 0.0 :
                reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);

        model.addAttribute("game", game);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewForm", new Review());
        model.addAttribute("averageRating", averageRating); // 그대로 double로 넘김

        return "games/detail";
    }

    @PostMapping("/games/{id}/reviews")
    public String addReview(@PathVariable Long id,
                            @ModelAttribute Review review,
                            HttpSession session) {
        Game game = gameRepository.findById(id).orElseThrow();

        Review newReview = new Review();
        newReview.setContent(review.getContent());
        newReview.setRating(review.getRating());

        // Java 8+ 방식의 instanceof 검사 및 형변환
        Object userObj = session.getAttribute("loggedInUser");
        if (userObj instanceof User) {
            User user = (User) userObj;
            newReview.setAuthor(user.getUserid()); // ✅ userid만 문자열로 저장
        } else {
            newReview.setAuthor("익명"); // 비로그인 또는 예외 상황 처리
        }

        newReview.setGame(game);
        reviewRepository.save(newReview);

        return "redirect:/games/" + id;
    }



    @GetMapping("/reviews/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Review review = reviewRepository.findById(id).orElseThrow();
        model.addAttribute("review", review);
        return "reviews/edit";
    }

    @PostMapping("/reviews/{id}/edit")
    public String updateReview(@PathVariable Long id, @ModelAttribute Review updatedReview) {
        Review review = reviewRepository.findById(id).orElseThrow();
        review.setAuthor(updatedReview.getAuthor());
        review.setContent(updatedReview.getContent());
        review.setRating(updatedReview.getRating());
        reviewRepository.save(review);
        return "redirect:/games/" + review.getGame().getId();
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        Review review = reviewRepository.findById(id).orElseThrow();
        Long gameId = review.getGame().getId();
        reviewRepository.deleteById(id);
        return "redirect:/games/" + gameId;
    }
}
