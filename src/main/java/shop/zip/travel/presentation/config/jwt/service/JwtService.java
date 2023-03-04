package shop.zip.travel.presentation.config.jwt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final String accessTokenSecretKey;
  private final String refreshTokenSecretKey;

  public JwtService(@Value("${spring.jwt.expiration[0].accessToken}") String accessTokenSecretKey,
      @Value("${spring.jwt.secretKey[1].refreshToken}") String refreshTokenSecretKey) {
    this.accessTokenSecretKey = accessTokenSecretKey;
    this.refreshTokenSecretKey = refreshTokenSecretKey;
  }
}
