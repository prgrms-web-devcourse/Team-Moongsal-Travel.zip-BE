package shop.zip.travel.domain.travelog.entity;

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
		validStartDate(startDate, endDate);
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public void validStartDate(LocalDateTime startDate, LocalDateTime endDate) {
		Assert.isTrue(startDate.isEqual(endDate) || startDate.isBefore(endDate),
			"날짜 입력이 잘못되었습니다.");
	}

}
