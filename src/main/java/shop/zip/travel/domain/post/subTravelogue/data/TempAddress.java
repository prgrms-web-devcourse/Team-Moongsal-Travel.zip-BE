package shop.zip.travel.domain.post.subTravelogue.data;

import shop.zip.travel.domain.post.data.DefaultValue;

public record TempAddress(
    String region
) {

  public Address toAddress() {
    return new Address(
        (region.isBlank()) ? DefaultValue.STRING.getValue() : region
    );
  }
}