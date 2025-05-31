package org.example.ad_project.controller;

import org.example.ad_project.repository.GameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping("/games")
    public String showGameList(Model model) {
        model.addAttribute("games", gameRepository.findAll());
        return "games/home";
    }
}
