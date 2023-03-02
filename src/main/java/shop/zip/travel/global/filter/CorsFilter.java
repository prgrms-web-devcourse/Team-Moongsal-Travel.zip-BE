//package shop.zip.travel.global.filter;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CorsFilter implements Filter {
//
//  private Logger log = LoggerFactory.getLogger(CorsFilter.class);
//
//  @Override
//  public void init(FilterConfig filterConfig) throws ServletException {
//
//  }
//
//  @Override
//  public void doFilter(
//      ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//    HttpServletRequest req = (HttpServletRequest) request;
//    HttpServletResponse res = (HttpServletResponse) response;
//
//    res.setHeader("Access-Control-Allow-Origin", "*");
//    res.setHeader("Access-Control-Allow-Credentials", "true");
//    res.setHeader("Access-Control-Allow-Methods","*");
//    res.setHeader("Access-Control-Max-Age", "3600");
//    res.setHeader("Access-Control-Allow-Headers", "*");
//
//    if("OPTIONS".equalsIgnoreCase(req.getMethod())) {
//      log.info("host : " + req.getRemoteHost());
//      log.info("addr : " + req.getRemoteAddr());
//      log.info("port : " + req.getRemotePort());
//      res.setStatus(HttpServletResponse.SC_OK);
//    }else {
//      chain.doFilter(req, res);
//    }
//  }
//
//  @Override
//  public void destroy() {
//
//  }
//}
