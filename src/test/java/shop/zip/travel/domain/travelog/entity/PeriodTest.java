package shop.zip.travel.domain.travelog.entity;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import shop.zip.travel.domain.post.travelogue.data.Period;

class PeriodTest {

	@DisplayName("startDate 가 endDate 보다 나중일 경우 IllegalArgumentException 을 던진다")
	@Test
	void verifyFailTest() {
		LocalDateTime startDate = LocalDateTime.of(2023, 2, 20, 6, 10);
		LocalDateTime endDate = LocalDateTime.of(2023, 2, 19, 6, 10);

		assertThatThrownBy(() -> new Period(startDate, endDate))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("startDate 가 endDate 와 같거나 이전 일 경우 Period 객체를 생성한다.")
	@ParameterizedTest
	@ValueSource(ints = {10, 19})
	void verifySuccessTest(int dayOfMonth) {

		LocalDateTime startDate = LocalDateTime.of(2023, 2, dayOfMonth, 6, 10);
		LocalDateTime endDate = LocalDateTime.of(2023, 2, 19, 6, 10);

		Period period = new Period(startDate, endDate);

		assertThat(period.getStartDate()).isEqualTo(startDate);
		assertThat(period.getEndDate()).isEqualTo(endDate);
	}

}