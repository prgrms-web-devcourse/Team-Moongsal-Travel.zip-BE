package shop.zip.travel.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import shop.zip.travel.global.filter.JwtAuthenticationFilter;
import shop.zip.travel.global.filter.JwtExceptionFilter;
import shop.zip.travel.global.oauth.CustomOAuth2UserService;
import shop.zip.travel.global.security.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;
  private final CustomOAuth2UserService customOAuth2UserService;

  public SecurityConfig(JwtTokenProvider jwtTokenProvider,
      CustomOAuth2UserService customOAuth2UserService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.customOAuth2UserService = customOAuth2UserService;
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers("/api/auth/**")
        .requestMatchers(HttpMethod.OPTIONS, "/api/**")
        .requestMatchers(HttpMethod.GET, "/api/travelogues/**")
        .requestMatchers(HttpMethod.GET, "/api/healths/**")
        .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
        .requestMatchers("/favicon.ico/**")
        .requestMatchers("/docs/index.html/**");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests((requests) -> requests
            .anyRequest().authenticated()
        )
        .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);

    http
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
