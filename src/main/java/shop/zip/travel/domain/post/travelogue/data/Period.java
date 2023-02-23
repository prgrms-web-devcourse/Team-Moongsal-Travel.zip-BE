package shop.zip.travel.domain.post.travelogue.data;

import java.time.LocalDateTime;

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

	public void verify(LocalDateTime startDate, LocalDateTime endDate) {
		verifyStartDate(startDate, endDate);
		verifyValidEndDate(endDate);
	}

	public void verifyStartDate(LocalDateTime startDate, LocalDateTime endDate) {
		Assert.isTrue(!startDate.isAfter(endDate),
			"날짜 입력이 잘못되었습니다.");
	}

	public void verifyValidEndDate(LocalDateTime endDate) {
		Assert.isTrue(!endDate.isAfter(LocalDateTime.now()), "날짜 입력이 잘못되었습니다");
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}
}
