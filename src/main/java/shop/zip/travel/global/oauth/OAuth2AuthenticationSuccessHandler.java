package shop.zip.travel.global.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.entity.Role;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.global.security.JwtTokenProvider;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

  private final ObjectMapper objectMapper;
  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;

  public OAuth2AuthenticationSuccessHandler( ObjectMapper objectMapper,
      MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
    this.objectMapper = objectMapper;
    this.memberRepository = memberRepository;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    var principal = authentication.getPrincipal();

    if (principal instanceof OAuth2User oauth2User) {

      Member member = saveOrUpdate(oauth2User.getAttributes());

      String targetUrl = determineTargetUrl(request, response, member);
      getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
  }

  private Member saveOrUpdate(Map<String, Object> attributes) {
    String email = attributes.get("email").toString();
    String profileImageUrl = attributes.get("profileImageUrl").toString();
    Member member = memberRepository.findByEmail(email)
        .map(entity -> entity.update(email, profileImageUrl))
        .orElse(toEntity(attributes));

    return memberRepository.save(member);
  }

  private Member toEntity(Map<String, Object> attributes) {
    return new Member(attributes.get("email").toString(), UUID.randomUUID().toString(),
        attributes.get("name").toString(), "입력필요", attributes.get("profileImageUrl").toString(),
        true, Role.USER, attributes.get("provider").toString(),
        attributes.get("id").toString());
  }

  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
      Member member) {
    String targetUrl = "https://travel-zip.vercel.app";

    return UriComponentsBuilder.fromOriginHeader(targetUrl)
        .queryParam("accessToken", jwtTokenProvider.createAccessToken(member.getId()))
        .queryParam("refreshToken", jwtTokenProvider.createRefreshToken())
        .build()
        .toUriString();
  }

}