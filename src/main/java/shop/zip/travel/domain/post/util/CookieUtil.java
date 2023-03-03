package shop.zip.travel.domain.post.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.springframework.web.util.WebUtils;

public class CookieUtil {

  public static final String VIEW_COUNT = "viewCount";
  public static final int DAY = 60 * 60 * 24;

  public static boolean canAddViewCount(HttpServletRequest request, HttpServletResponse response,
      Long travelogueId) {
    if (hasViewCountCookie(request, travelogueId)) {
      Cookie cookie = new Cookie(VIEW_COUNT, String.valueOf(travelogueId));
      cookie.setMaxAge(DAY);
      response.addCookie(cookie);
      return true;
    }
    return false;
  }

  private static boolean hasViewCountCookie(HttpServletRequest request, Long travelogueId) {

    Cookie cookie = WebUtils.getCookie(request, VIEW_COUNT);

    if (Objects.isNull(cookie)) {
      return true;
    }
    return !cookie.getValue().equals(String.valueOf(travelogueId));
  }

}
