package org.example.ad_project.service;

import jakarta.servlet.http.HttpSession;
import org.example.ad_project.entity.User;
import org.example.ad_project.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String userid, String password, HttpSession session) {
        User user = userRepository.findByUserid(userid);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loggedInUser", user);
            return true;
        }
        return false;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}