package shop.zip.travel.domain.post.data;

import java.util.Objects;

public record TempCountry(
    String name
) {

  public Country toCountry() {
    return new Country(
        (Objects.isNull(name)) ? DefaultValue.STRING.getValue() : name
    );
  }
}
