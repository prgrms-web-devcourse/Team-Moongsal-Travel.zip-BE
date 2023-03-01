package shop.zip.travel.domain.post.travelogue.dto.req;

import jakarta.validation.constraints.NotNull;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.data.DefaultValue;
import shop.zip.travel.domain.post.data.TempCountry;
import shop.zip.travel.domain.post.travelogue.data.temp.TempCost;
import shop.zip.travel.domain.post.travelogue.data.temp.TempPeriod;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

public record TempTravelogueCreateReq(
  @NotNull
  TempPeriod period,
  String title,
  @NotNull
  TempCountry country,
  String thumbnail,
  @NotNull
  TempCost cost
) {

  private static final boolean TEMP_SAVE_STATUS = false;

  public Travelogue toTravelogue(Member member) {
    return new Travelogue(
      period.toPeriod(),
      (title == null) ? DefaultValue.STRING.getValue() : title,
      country.toCountry(),
      (thumbnail == null) ? DefaultValue.STRING.getValue() : thumbnail,
      cost.toCost(),
      TEMP_SAVE_STATUS,
      member
    );
  }
}
