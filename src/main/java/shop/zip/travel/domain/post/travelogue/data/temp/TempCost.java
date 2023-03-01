package shop.zip.travel.domain.post.travelogue.data.temp;

import shop.zip.travel.domain.post.data.DefaultValue;
import shop.zip.travel.domain.post.travelogue.data.Cost;

public record TempCost(

  Long lodge,
  Long transportation,
  Long etc,
  Long total

) {

  public Cost toCost() {
    return new Cost(
      (lodge == null) ? Long.parseLong(DefaultValue.LONG.getValue()) : lodge,
      (transportation == null) ? Long.parseLong(DefaultValue.LONG.getValue()) : transportation,
      (etc == null) ? Long.parseLong(DefaultValue.LONG.getValue()) : etc,
      (total == null) ? Long.parseLong(DefaultValue.LONG.getValue()) : total
    );
  }
}
