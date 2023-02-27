package shop.zip.travel.domain.post.data;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

class CountryTest {

  private static Logger log = LoggerFactory.getLogger(CountryTest.class);

  private final String KOREAN = "ko";

  private final List<String> LOCALES = Arrays.stream(Locale.getISOCountries())
      .map(country -> new Locale(KOREAN, country).getDisplayCountry())
      .toList();

  @Test
  void getName() {
    System.out.println("자 준비하시고 쏘세요!!");
    LOCALES.forEach(System.out::println);
    Assert.isTrue(LOCALES.contains("일본"), "국가명을 확인해주세요");

  }
}