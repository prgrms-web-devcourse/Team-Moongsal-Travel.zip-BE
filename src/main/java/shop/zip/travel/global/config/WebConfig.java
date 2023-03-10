package shop.zip.travel.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry corsRegistry) {
    corsRegistry.addMapping("/**")
        // "http://localhost:3000","https://travel-zip.vercel.app"
//        .allowedOrigins("http://localhost:3000","https://travel-zip.vercel.app")
        .allowedOrigins("*")
        .allowedMethods("*")
        .allowedHeaders("*")
        .allowCredentials(false)
        .maxAge(3600);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/docs/**").addResourceLocations("classpath:/static/docs/");
  }
}
