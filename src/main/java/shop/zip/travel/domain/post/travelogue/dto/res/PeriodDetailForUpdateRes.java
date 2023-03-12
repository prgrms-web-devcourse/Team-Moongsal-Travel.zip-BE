package shop.zip.travel.domain.post.travelogue.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import shop.zip.travel.domain.post.travelogue.data.Period;

public record PeriodDetailForUpdateRes(
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate startDate,

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate endDate
) {

  public static PeriodDetailForUpdateRes toDto(Period period) {
    return new PeriodDetailForUpdateRes(
        period.getStartDate(),
        period.getEndDate()
    );
  }
}
