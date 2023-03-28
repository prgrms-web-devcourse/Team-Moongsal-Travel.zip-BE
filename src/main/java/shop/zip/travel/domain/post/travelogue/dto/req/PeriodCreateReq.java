package shop.zip.travel.domain.post.travelogue.dto.req;

import java.time.LocalDate;
import shop.zip.travel.domain.post.travelogue.data.Period;

public record PeriodCreateReq(
    LocalDate startDate,
    LocalDate endDate
) {

  public Period toPeriod() {
    return new Period(
        this.startDate,
        this.endDate
    );
  }
}
