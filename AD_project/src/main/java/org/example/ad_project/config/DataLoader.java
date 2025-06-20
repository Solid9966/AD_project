package org.example.ad_project.config;

import jakarta.annotation.PostConstruct;
import org.example.ad_project.entity.Game;
import org.example.ad_project.repository.GameRepository;
import org.example.ad_project.repository.ReviewRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader {

    private final GameRepository gameRepository;
    private final ReviewRepository reviewRepository;

    public DataLoader(GameRepository gameRepository, ReviewRepository reviewRepository) {
        this.gameRepository = gameRepository;
        this.reviewRepository = reviewRepository;
    }

    @PostConstruct
    public void loadSampleData() {
        if (gameRepository.count() > 0) return;  // ⚠ 이미 데이터 있으면 중단

        gameRepository.deleteAll();  // 선택: 진짜 재초기화 원할 경우만 유지
        reviewRepository.deleteAll();

        gameRepository.saveAll(List.of(
                new Game("발로란트", "FPS 게임", "https://playvalorant.com/ko-kr/"),
                new Game("롤", "MOBA 게임", "https://www.leagueoflegends.com/ko-kr/"),
                new Game("FC 온라인", "스포츠 게임", "https://fconline.nexon.com/main/index")
        ));
    }

}

