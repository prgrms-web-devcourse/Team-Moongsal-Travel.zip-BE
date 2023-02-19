package shop.zip.travel.domain.travelog.entity;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PeriodTest {

	@DisplayName("startDate 가 endDate 보다 나중일 경우 IllegalArgumentException 을 던진다")
	@Test
	void validTest() {
		LocalDateTime startDate = LocalDateTime.of(2023, 2, 20, 6, 10, 10);
		LocalDateTime endDate = LocalDateTime.of(2023, 2, 19, 6, 10, 0);

		Assertions.assertThatThrownBy(() -> new Period(startDate, endDate))
			.isInstanceOf(IllegalArgumentException.class);
	}
}