package shop.mtcoding.blog._core.interceptor;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import shop.mtcoding.blog._core.errors.exception.Exception401;
import shop.mtcoding.blog._core.errors.exception.Exception500;
import shop.mtcoding.blog._core.utils.JwtUtil;
import shop.mtcoding.blog.user.User;

// /api/** 인증 필요 주소
public class LoginInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Bearer jwt토큰
        String jwt = request.getHeader("Authorization");

        if(jwt == null){
            throw new Exception401("jwt 토큰을 전달해주세요");
        }

        jwt = jwt.replace("Bearer ", "");

        // 검증
        try {
            User sessionUser = JwtUtil.verify(jwt);

            // 임시 세션 (jsessionId는 필요 없음)
            HttpSession session = request.getSession();
            session.setAttribute("sessionUser", sessionUser);
            return true;
        }catch (TokenExpiredException e){
            throw new Exception401("토큰 만료시간이 지났어요. 다시 로그인하세요");
        }catch (JWTDecodeException e){
            throw new Exception401("토큰이 유효하지 않습니다");
        }catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }
}