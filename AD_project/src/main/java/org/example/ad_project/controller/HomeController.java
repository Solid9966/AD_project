package org.example.ad_project.controller;
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
    public String showGameList(Model model) {
        model.addAttribute("games", gameRepository.findAll()); // 여기서 games 값을 가져와서 전달
        return "games/home"; // templates/games/home.html 렌더링
    }
}
