package org.example.ad_project.controller;

import jakarta.servlet.http.HttpSession;
import org.example.ad_project.entity.Game;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.example.ad_project.repository.GameRepository;


import java.util.List;

@Controller
public class HomeController {

    private final GameRepository gameRepository;

    public HomeController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping("/")
    public String showGameList(Model model, HttpSession session) {
        Object loggedInUser = session.getAttribute("loggedInUser");

        // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("games", gameRepository.findAll());
        return "games/home"; // 로그인한 경우 게임 목록 페이지로 이동
    }
}
