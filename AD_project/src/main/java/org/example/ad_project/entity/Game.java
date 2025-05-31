package org.example.ad_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

    // 기본 생성자 (JPA용)
    public Game() {}

    // 생성자 추가
    public Game(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }
}
