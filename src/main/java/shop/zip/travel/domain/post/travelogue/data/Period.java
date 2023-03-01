package shop.zip.travel.domain.post.travelogue.data;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import shop.zip.travel.domain.post.data.DefaultValue;

@Embeddable
public class Period {

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    protected Period() {
    }

    public Period(LocalDate startDate, LocalDate endDate) {
        verify(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void verify(LocalDate startDate, LocalDate endDate) {
        notNull(startDate, "출발날짜를 확인해주세요");
        notNull(endDate, "도착날짜를 확인해주세요");
        verifyStartDateIsBeforeEndDate(startDate, endDate);
        verifyEndDateIsBeforeToday(endDate);
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
        return ChronoUnit.DAYS.between(this.startDate, this.endDate);
    }

    public boolean cannotPublish() {
        return DefaultValue.LOCAL_DATE.isEqual(startDate.toString()) ||
            DefaultValue.LOCAL_DATE.isEqual(endDate.toString());
    }
}
