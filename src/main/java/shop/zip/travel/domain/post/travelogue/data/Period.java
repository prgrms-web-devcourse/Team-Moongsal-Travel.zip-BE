package shop.zip.travel.domain.post.travelogue.data;

import static org.springframework.util.Assert.isTrue;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Embeddable
public class Period {

  private static final long NO_DATE = -1L;

  @Column
  private LocalDate startDate;

  @Column
  private LocalDate endDate;

  protected Period() {
  }

  public Period(LocalDate startDate, LocalDate endDate) {
    if (!Objects.isNull(startDate) && !Objects.isNull(endDate)) {
      verifyEndDateIsBeforeToday(endDate);
      verifyStartDateIsBeforeEndDate(startDate, endDate);
    }
    this.startDate = startDate;
    this.endDate = endDate;
  }

  private void verifyStartDateIsBeforeEndDate(LocalDate startDate, LocalDate endDate) {
    isTrue(!startDate.isAfter(endDate), "날짜 입력이 잘못되었습니다.");
  }

  private void verifyEndDateIsBeforeToday(LocalDate endDate) {
    isTrue(!endDate.isAfter(LocalDate.now()), "날짜 입력이 잘못되었습니다");
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public long getNights() {
    if (hasNull()) {
      return NO_DATE;
    }
    return ChronoUnit.DAYS.between(this.startDate, this.endDate);
  }

  public boolean cannotPublish() {
    verifyEndDateIsBeforeToday(endDate);
    verifyStartDateIsBeforeEndDate(startDate, endDate);
    return hasNull();
  }

  private boolean hasNull() {
    return Objects.isNull(startDate) || Objects.isNull(endDate);
  }
}
