package shop.mtcoding.loginapp.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Profile;

import java.security.Timestamp;
import java.util.Properties;

public class NaverResponse {

    @Data
    public static class TokenDTO{
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("expires_in")
        private Integer expiresIn;
        @JsonProperty("error")
        private String errorCode;
        @JsonProperty("error_description")
        private String errorMessage;
    }

    @Data
    public static class NaverUserDTO{
        private Long id;

        @JsonProperty("response")
        private Properties properties;

        @Data
        class Properties{
            private String name;
        }
    }
}
