package shop.mtcoding.loginapp.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final shop.mtcoding.loginapp.user.UserService userService;
    private final HttpSession session;

    @GetMapping("/join-form")
    public String joinForm(){

        return "join-form";
    }

    @GetMapping("/login-form")
    public String loginForm(){

        return "login-form";
    }

    @PostMapping("/join")
    public String join(String username, String password, String email){
        userService.회원가입(username,password,email);

        return "redirect:/login-form";
    }

    @PostMapping("/login")
    public String join(String username, String password){
        User sessionUser = userService.로그인(username,password);
        session.setAttribute("sessionUser",sessionUser);

        return "redirect:/shop";
    }

    // 쿼리스트링으로 오길 기대

    // 브라우저가 자동으로 실행함
    @GetMapping("/oauth/callback")
    public String oauthCallback( String code){

        User sessionUser = userService.카카오로그인(code);
        System.out.println("콜백됐다!! : " + code);
        session.setAttribute("sessionUser",sessionUser);

        return "redirect:/shop";
    }

}
