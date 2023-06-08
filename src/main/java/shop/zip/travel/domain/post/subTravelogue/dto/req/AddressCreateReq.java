package shop.zip.travel.domain.post.subTravelogue.dto.req;

import shop.zip.travel.domain.post.subTravelogue.data.Address;

public record AddressCreateReq(
    String region
) {

  public Address toAddress() {
    return new Address(this.region);
  }
}