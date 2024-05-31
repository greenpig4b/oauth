package shop.mtcoding.loginapp.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@Table(name = "user_tb")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private String address; // 상품구매 할 시 정보받기
    private String provider; // facebook, kakao, apple, naver -> 제공받음

    @Builder
    public User(Integer id, String username, String password, String email, String address, String provider) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.provider = provider;
    }
}
