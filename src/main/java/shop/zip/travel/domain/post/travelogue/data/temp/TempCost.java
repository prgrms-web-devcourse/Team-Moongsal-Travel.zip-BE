package shop.zip.travel.domain.post.travelogue.data.temp;

import java.util.Objects;
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
        (Objects.isNull(lodge)) ? Long.parseLong(DefaultValue.LONG.getValue()) : lodge,
        (Objects.isNull(transportation)) ? Long.parseLong(DefaultValue.LONG.getValue())
            : transportation,
        (Objects.isNull(etc)) ? Long.parseLong(DefaultValue.LONG.getValue()) : etc,
        (Objects.isNull(total)) ? Long.parseLong(DefaultValue.LONG.getValue()) : total
    );
  }
}
