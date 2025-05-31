package org.example.ad_project.controller;
import org.example.ad_project.entity.User;
import org.example.ad_project.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //로그인 폼
    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }

    //로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String userid,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        User user = userRepository.findByUserid(userid);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/";
        } else {
            model.addAttribute("error", "ID 또는 비밀번호가 일치하지 않습니다.");
            return "user/login";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // 회원가입 폼
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String register(@RequestParam String userid,
                           @RequestParam String password,
                           @RequestParam String email,
                           Model model) {
        if (userRepository.findByUserid(userid) != null) {
            model.addAttribute("error", "이미 사용 중인 ID입니다.");
            return "register";
        }

        User newUser = new User();
        newUser.setUserid(userid);
        newUser.setPassword(password);  // 실무에서는 BCrypt 등으로 암호화 필요
        newUser.setEmail(email);

        userRepository.save(newUser);
        return "redirect:/login";
    }
}
