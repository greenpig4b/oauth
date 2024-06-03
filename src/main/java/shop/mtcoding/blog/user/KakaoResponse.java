package shop.mtcoding.blog.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.security.Timestamp;

public class KakaoResponse {

    @Data
    public static class KakaoUserDTO {
        private Long id;
        @JsonProperty("connected_at")
        private Timestamp connectedAt;
        private Properties properties;

        @Data
        class Properties {
            private String nickname;
        }
    }
}
