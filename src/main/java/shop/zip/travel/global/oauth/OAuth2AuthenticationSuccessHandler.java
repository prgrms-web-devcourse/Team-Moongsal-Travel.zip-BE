package shop.zip.travel.global.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

  private final ObjectMapper objectMapper;

  public OAuth2AuthenticationSuccessHandler( ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    var principal = authentication.getPrincipal();

    if (principal instanceof OAuth2User oauth2User) {

      Map<String, Object> properties = oauth2User.getAttribute("properties");

      log.info(properties.get("nickname").toString());


      response.setStatus(HttpStatus.OK.value());
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(objectMapper.writeValueAsString(null));
    }
  }

  private String parseProviderName(HttpServletRequest request) {
    var splitURI = request.getRequestURI().split("/");
    return splitURI[splitURI.length - 1];
  }

}