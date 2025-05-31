package org.example.ad_project.repository;

import org.example.ad_project.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByGame_Id(Long gameId);
}
