package me.philocsera.springbootdeveloper.config.jwt;

import io.jsonwebtoken.Jwts;
import me.philocsera.springbootdeveloper.domain.User;
import me.philocsera.springbootdeveloper.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken")
    @Test
    void generateToken(){
        User testUser = userRepository.save(User.builder()
                .email("philoc@gmail.com")
                .password("stargazer")
                .build());

        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    @DisplayName("validToken_invalid")
    @Test
    void validToken_invalidToken(){
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        boolean rslt = tokenProvider.validToken(token);

        assertThat(rslt).isFalse();
    }

    @DisplayName("validToken_valid")
    @Test
    void validToken_validToken(){
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);

        boolean rslt = tokenProvider.validToken(token);

        assertThat(rslt).isTrue();
    }

    @DisplayName("getAuthentication")
    @Test
    void getAuthentication(){
        String userEmail = "philoc@gmail.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        Authentication authentication = tokenProvider.getAuthentication(token);

        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).
                isEqualTo(userEmail);
    }

    @DisplayName("getUserId")
    @Test
    void getUserId(){
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id",userId))
                .build()
                .createToken(jwtProperties);

        Long userIdByToken = tokenProvider.getUserId(token);

        assertThat(userIdByToken).isEqualTo(userId);
    }
}
