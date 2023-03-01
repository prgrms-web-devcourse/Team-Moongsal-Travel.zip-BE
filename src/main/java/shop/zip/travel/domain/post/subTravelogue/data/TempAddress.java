package shop.zip.travel.domain.post.subTravelogue.data;

import shop.zip.travel.domain.post.data.DefaultValue;

public record TempAddress(
    String spot
) {

  public Address toAddress() {
    return new Address(
        (spot == null) ? DefaultValue.STRING.getValue() : spot
    );
  }
}