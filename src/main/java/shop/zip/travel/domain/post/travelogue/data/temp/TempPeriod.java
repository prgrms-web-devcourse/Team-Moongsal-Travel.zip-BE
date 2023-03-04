package shop.zip.travel.domain.post.travelogue.data.temp;

import java.time.LocalDate;
import java.util.Objects;
import shop.zip.travel.domain.post.data.DefaultValue;
import shop.zip.travel.domain.post.travelogue.data.Period;

public record TempPeriod(

  LocalDate startDate,
  LocalDate endDate

) {

  public Period toPeriod() {
    return new Period(
        (Objects.isNull(startDate)) ? LocalDate.parse(DefaultValue.LOCAL_DATE.getValue())
            : startDate,
        (Objects.isNull(endDate)) ? LocalDate.parse(DefaultValue.LOCAL_DATE.getValue()) : endDate
    );
  }
}
