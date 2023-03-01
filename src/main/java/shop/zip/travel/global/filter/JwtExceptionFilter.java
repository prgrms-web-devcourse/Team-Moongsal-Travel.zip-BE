package shop.zip.travel.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import shop.zip.travel.domain.member.exception.InvalidAccessTokenException;
import shop.zip.travel.global.error.ErrorResponse;

public class JwtExceptionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      filterChain.doFilter(request, response);
    } catch (InvalidAccessTokenException e) {
      response.setStatus(e.getErrorCode().getStatusValue());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");
      objectMapper.writeValue(response.getWriter(),
          new ErrorResponse(e.getErrorCode().getMessage()));
    }
  }
}

