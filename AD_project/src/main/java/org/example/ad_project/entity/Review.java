package org.example.ad_project.entity;

import jakarta.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private int rating;
    private String author;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    public Review() {}

    public Review(String content, int rating, String author, Game game) {
        this.content = content;
        this.rating = rating;
        this.author = author;
        this.game = game;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
}
