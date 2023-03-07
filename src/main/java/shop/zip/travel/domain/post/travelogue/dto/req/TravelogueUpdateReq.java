package shop.zip.travel.domain.post.travelogue.dto.req;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import shop.zip.travel.domain.post.data.DefaultValue;
import shop.zip.travel.domain.post.data.TempCountry;
import shop.zip.travel.domain.post.travelogue.data.temp.TempCost;
import shop.zip.travel.domain.post.travelogue.data.temp.TempPeriod;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueUpdate;

public record TravelogueUpdateReq(
    @NotNull
    TempPeriod period,
    String title,
    @NotNull
    TempCountry country,
    String thumbnail,
    @NotNull
    TempCost cost
) {

  public TravelogueUpdate toTravelogueUpdate() {
    return new TravelogueUpdate(
        period.toPeriod(),
        (Objects.isNull(title)) ? DefaultValue.STRING.getValue() : title,
        country.toCountry(),
        (Objects.isNull(thumbnail)) ? DefaultValue.STRING.getValue() : thumbnail,
        cost.toCost()
    );
  }
}
