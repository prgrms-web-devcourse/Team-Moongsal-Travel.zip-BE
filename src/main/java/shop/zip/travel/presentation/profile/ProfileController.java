package shop.zip.travel.presentation.profile;

import java.util.Arrays;
import java.util.List;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

  private final Environment environment;

  public ProfileController(Environment environment) {
    this.environment = environment;
  }

  @GetMapping
  public String profile() {
    List<String> profiles = Arrays.asList(environment.getActiveProfiles());
    List<String> devProfiles = Arrays.asList("dev", "sub");
    String defaultProfile = profiles.isEmpty() ? "local" : profiles.get(0);

    return profiles.stream().filter(devProfiles::contains).findAny().orElse(defaultProfile);
  }
}
