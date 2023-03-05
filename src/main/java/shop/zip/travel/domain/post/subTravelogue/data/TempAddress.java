package shop.zip.travel.domain.post.subTravelogue.data;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import shop.zip.travel.domain.post.data.DefaultValue;

public record TempAddress(
    @NotBlank
    String region
) {

  public Address toAddress() {
    return new Address(
        (Objects.isNull(region)) ? DefaultValue.STRING.getValue() : region
    );
  }
}