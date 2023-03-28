package shop.zip.travel.domain.post.travelogue.dto.req;

import jakarta.validation.constraints.NotNull;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

public record TravelogueCreateReq(
    @NotNull
    PeriodCreateReq period,
    String title,
    @NotNull
    Country country,
    String thumbnail,
    @NotNull
    CostCreateReq cost
) {

  private static final boolean TEMP_SAVE_STATUS = false;

  public Travelogue toTravelogue(Member member) {
    return new Travelogue(
        period.toPeriod(),
        title,
        country,
        thumbnail,
        cost.toCost(),
        TEMP_SAVE_STATUS,
        member
    );
  }
}
