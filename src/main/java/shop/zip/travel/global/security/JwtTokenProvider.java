package shop.zip.travel.global.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import shop.zip.travel.domain.member.exception.InvalidAccessTokenException;
import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.JsonNotParsingException;

@Component
public class JwtTokenProvider {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  private final String accessTokenSecretKey;
  private final String refreshTokenSecretKey;

  private final long ACCESS_TOKEN_EXPIRED_TIME = Duration.ofHours(1).toMillis();
  private final long REFRESH_TOKEN_EXPIRED_TIME = Duration.ofHours(2).toMillis();

  private final CustomUserDetailsService customUserDetailsService;

  public JwtTokenProvider(
      @Value("${spring.jwt.accessTokenSecretKey}") String accessTokenSecretKey,
      @Value("${spring.jwt.refreshTokenSecretKey}") String refreshTokenSecretKey,
      CustomUserDetailsService customUserDetailsService) {
    this.accessTokenSecretKey = accessTokenSecretKey;
    this.refreshTokenSecretKey = refreshTokenSecretKey;
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
      throw new InvalidAccessTokenException(ErrorCode.INVALID_ACCESS_TOKEN);
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

  public String createAccessToken(Long memberId) {
    Claims claims = Jwts.claims();
    claims.setSubject("Travel.zip");
    claims.put("memberId", memberId);

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRED_TIME);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, accessTokenSecretKey)
        .compact();
  }

  public boolean validateRefreshToken(String refreshToken) {
    try {
      Jwts.parser().setSigningKey(refreshTokenSecretKey).parseClaimsJws(refreshToken);
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

  public String createRefreshToken() {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRED_TIME);

    return Jwts.builder()
        .setSubject("Travel.zip")
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, refreshTokenSecretKey)
        .compact();
  }

  public String getMemberIdUsingDecode(String accessToken) {
    String[] chunks = accessToken.split("\\.");
    Base64.Decoder decoder = Base64.getUrlDecoder();

    String payload = new String(decoder.decode(chunks[1]));

    ObjectMapper mapper = new ObjectMapper();
    try {
      Map<String, Object> returnMap = mapper.readValue(payload, Map.class);
      return returnMap.get("memberId").toString();
    } catch (JsonProcessingException e) {
      throw new JsonNotParsingException(ErrorCode.JSON_NOT_PARSING);
    }

  }
}
