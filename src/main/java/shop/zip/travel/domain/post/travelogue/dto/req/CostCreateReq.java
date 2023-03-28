package shop.zip.travel.domain.post.travelogue.dto.req;

import shop.zip.travel.domain.post.travelogue.data.Cost;

public record CostCreateReq(
    Long transportation,
    Long lodge,
    Long etc,
    Long total
) {

  public Cost toCost() {
    return new Cost(
        transportation,
        lodge,
        etc,
        total
    );
  }
}
