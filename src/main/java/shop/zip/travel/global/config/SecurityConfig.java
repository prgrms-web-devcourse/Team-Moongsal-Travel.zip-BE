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
import shop.zip.travel.global.oauth.OAuth2AuthenticationSuccessHandler;
import shop.zip.travel.global.security.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

  public SecurityConfig(JwtTokenProvider jwtTokenProvider,
      CustomOAuth2UserService customOAuth2UserService,
      OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.customOAuth2UserService = customOAuth2UserService;
    this.oauth2AuthenticationSuccessHandler = oauth2AuthenticationSuccessHandler;
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers("/api/auth/**")
        .requestMatchers("/docs/rest-docs.html")
        .requestMatchers(HttpMethod.OPTIONS, "/api/**")
        .requestMatchers(HttpMethod.GET, "/api/travelogues/**")
        .requestMatchers(HttpMethod.GET, "/api/healths/**")
        .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
        .requestMatchers("/favicon.ico/**")
        .requestMatchers("/docs/index.html/**")
        .requestMatchers("/favicon.ico")
        ;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()
        .csrf().disable()
        .formLogin().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .oauth2Login()
        .authorizationEndpoint().baseUri("/oauth2/authorize")
        .and()
        .redirectionEndpoint()
        .baseUri("/*/*/oauth2/code/*")
        .and()
        .userInfoEndpoint().userService(customOAuth2UserService)
        .and()
        .successHandler(oauth2AuthenticationSuccessHandler)
        .and()
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/docs/index.html").permitAll()
            .anyRequest().authenticated()
        )
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
