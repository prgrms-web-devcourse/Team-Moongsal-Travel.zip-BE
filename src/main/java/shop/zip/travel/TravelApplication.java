package shop.zip.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TravelApplication {

  public static void main(String[] args) {
    SpringApplication.run(TravelApplication.class, args);
  }

}
