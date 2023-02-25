package shop.zip.travel.domain.post.travelogue.data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Period {

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    protected Period() {
    }

    public Period(LocalDateTime startDate, LocalDateTime endDate) {
        verify(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void verify(LocalDateTime startDate, LocalDateTime endDate) {
        Assert.notNull(startDate, "출발날짜를 확인해주세요");
        Assert.notNull(endDate, "도착날짜를 확인해주세요");
        verifyStartDateIsBeforeEndDate(startDate, endDate);
        verifyEndDateIsBeforeToday(endDate);
    }

    private void verifyStartDateIsBeforeEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        Assert.isTrue(!startDate.isAfter(endDate), "날짜 입력이 잘못되었습니다.");
    }

    private void verifyEndDateIsBeforeToday(LocalDateTime endDate) {
        Assert.isTrue(!endDate.isAfter(LocalDateTime.now()), "날짜 입력이 잘못되었습니다");
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public int getNights(){
		return (int)ChronoUnit.DAYS.between(this.startDate, this.endDate);
	}
}
