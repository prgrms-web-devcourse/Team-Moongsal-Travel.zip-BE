package shop.zip.travel.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.Duration;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  private final String accessTokenSecretKey;

  private final long ACCESS_TOKEN_EXPIRED_TIME = Duration.ofMinutes(5).toMillis();

  private final CustomUserDetailsService customUserDetailsService;

  public JwtTokenProvider(@Value("${spring.jwt.accessTokenSecretKey}") String accessTokenSecretKey,
      CustomUserDetailsService customUserDetailsService) {
    this.accessTokenSecretKey = accessTokenSecretKey;
    this.customUserDetailsService = customUserDetailsService;
  }

  public String removeBearer(String bearerToken) {
    return bearerToken.substring("Bearer ".length());
  }

  public boolean validateAccessToken(String accessToken) {
    try {
      Jwts.parser().setSigningKey(accessTokenSecretKey).parseClaimsJws(accessToken);
      return true;
    } catch (SignatureException ex) {
      logger.error("유효하지 않은 JWT 서명");
    } catch (MalformedJwtException ex) {
      logger.error("유효하지 않은 JWT 토큰");
    } catch (ExpiredJwtException ex) {
      logger.error("만료된 JWT 토큰");
    } catch (UnsupportedJwtException ex) {
      logger.error("지원하지 않는 JWT 토큰");
    } catch (IllegalArgumentException ex) {
      logger.error("비어있는 토큰");
    }
    return false;
  }

  public String getMemberId(String accessToken) {
    return Jwts.parser().setSigningKey(accessTokenSecretKey).parseClaimsJws(accessToken)
        .getBody().get("memberId").toString();
  }

  public Authentication getAuthentication(String token) {
    Long memberId = Long.parseLong(getMemberId(token));
    UserDetails userDetails = customUserDetailsService.loadUserById(memberId);
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  // JWT 을 만들어내는 메소드
  public String createToken(Long memberId) {
    Claims claims = Jwts.claims();
    claims.put("memberId", memberId);
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRED_TIME);

    // Token : header / payload / signature
    // 이 중에서 payload 에 해당하는 부분을 작성하는 부분과 signature 부분 생성
    return Jwts.builder()
        .setSubject("Travel.zip")
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, accessTokenSecretKey)
        .compact();
  }
}
