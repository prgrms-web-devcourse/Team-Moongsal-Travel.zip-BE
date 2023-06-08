package shop.zip.travel.domain.post.travelogue.dto.req;

import shop.zip.travel.domain.post.data.Country;

public record CountryCreateReq(
    String name
) {

  public Country toCountry() {
    return new Country(name);
  }
}
