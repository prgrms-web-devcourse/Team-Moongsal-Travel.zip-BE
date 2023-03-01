package shop.zip.travel.domain.post.travelogue.data.temp;

import java.time.LocalDate;
import shop.zip.travel.domain.post.data.DefaultValue;
import shop.zip.travel.domain.post.travelogue.data.Period;

public record TempPeriod(

  LocalDate startDate,
  LocalDate endDate

) {

  public Period toPeriod() {
    return new Period(
      (startDate == null) ? LocalDate.parse(DefaultValue.LOCAL_DATE.getValue()) : startDate,
      (endDate == null) ? LocalDate.parse(DefaultValue.LOCAL_DATE.getValue()) : endDate
    );
  }
}
