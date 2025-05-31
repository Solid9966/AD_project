package org.example.ad_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;
    private String url;

    // ✅ 평점 (1~5 범위 등)
    private Integer rating;

    // ✅ 후기 내용
    @Column(length = 1000)
    private String review;

    // 기본 생성자
    public Game() {}

    // 생성자 (후기/평점 포함)
    public Game(String title, String description, String url, Integer rating, String review) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.rating = rating;
        this.review = review;
    }

    // 생성자 (기존용)
    public Game(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }
}
