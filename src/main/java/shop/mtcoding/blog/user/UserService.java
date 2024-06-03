package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import shop.mtcoding.blog._core.errors.exception.Exception400;
import shop.mtcoding.blog._core.errors.exception.Exception401;
import shop.mtcoding.blog._core.errors.exception.Exception404;
import shop.mtcoding.blog._core.utils.JwtUtil;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service // IoC 등록
public class UserService {

    private final UserJPARepository userJPARepository;

    public String 로그인(UserRequest.LoginDTO reqDTO){
        User user = userJPARepository.findByUsernameAndPassword(reqDTO.getUsername(), reqDTO.getPassword())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));

        String jwt = JwtUtil.create(user);
        return jwt;
    }

    @Transactional
    public UserResponse.DTO 회원가입(UserRequest.JoinDTO reqDTO){ // ssar
        // 1. 유저네임 중복검사 (서비스 체크) - DB연결이 필요한 것은 Controller에서 작성할 수 없다.
        Optional<User> userOP = userJPARepository.findByUsername(reqDTO.getUsername());

        if(userOP.isPresent()){
            throw new Exception400("중복된 유저네임입니다");
        }

        // 2. 회원가입
        User user = userJPARepository.save(reqDTO.toEntity());

        return new UserResponse.DTO(user);
    }

    /**
     *         1. 카카오에서 사용자 정보 요청하기
     *         2. code 방식과 동일
     *         3. jwt(스프링서버) 생성해서 엡에게 전달
     */
    public String 카카오로그인(String kakaoAccessToken) {

        // 1. RestTemplate 객체 생성
        RestTemplate rt = new RestTemplate();

        // 2. 토큰으로 사용자 정보 받기 (PK, Email)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer "+kakaoAccessToken);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(headers);

        ResponseEntity<KakaoResponse.KakaoUserDTO> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                KakaoResponse.KakaoUserDTO.class);

        // 3. 해당정보로 DB조회 (있을수, 없을수)
        String username = "kakao_"+response.getBody().getId();
        User userPS = userJPARepository.findByUsername(username)
                .orElse(null);

        // 4. 있으면? - 조회된 유저정보 리턴
        if(userPS != null){
            return JwtUtil.create(userPS);
        }else{
            // 5. 없으면? - 강제 회원가입
            User user = User.builder()
                    .username(username)
                    .password(UUID.randomUUID().toString())
                    .email(response.getBody().getProperties().getNickname()+"@nate.com")
//                    .provider("kakao")
                    .build();
            User returnUser = userJPARepository.save(user);
            return JwtUtil.create(returnUser);
        }


    }
}