package me.philocsera.springbootdeveloper.repository;

import me.philocsera.springbootdeveloper.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findById(Long userId);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
