package org.example.ad_project.controller;

import jakarta.servlet.http.HttpSession;
import org.example.ad_project.entity.User;
import org.example.ad_project.service.AuthService;
import org.example.ad_project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String userid,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        boolean result = authService.login(userid, password, session);
        if (result) {
            return "redirect:/";
        } else {
            model.addAttribute("error", "ID 또는 비밀번호가 일치하지 않습니다.");
            return "user/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        authService.logout(session);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String userid,
                           @RequestParam String password,
                           @RequestParam String email,
                           Model model) {
        if (userService.getUserByUserid(userid) != null) {
            model.addAttribute("error", "이미 사용 중인 ID입니다.");
            return "register";
        }

        User newUser = new User();
        newUser.setUserid(userid);
        newUser.setPassword(password);
        newUser.setEmail(email);

        userService.saveUser(newUser);
        return "redirect:/login";
    }
}
