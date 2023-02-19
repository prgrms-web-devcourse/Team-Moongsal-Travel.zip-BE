package shop.zip.travel.domain.travelog.entity;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TravelogueTest {

	@Test
	@DisplayName("존재하지 않는 나라명을 입력할 경우 IllegalArgumentException 을 던진다")
	void validCountry() {
		Period period = new Period(LocalDateTime.MIN, LocalDateTime.MAX);
		String title = "테스트";
		String country = "짱구네";
		Long totalCost = 10000L;
		String thumbnail = "";

		Assertions.assertThatThrownBy(() -> new Travelogue(period, title, country, totalCost, thumbnail))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("총금액이 0원 이하일 경우 IllegalArgumentException 을 던진다")
	void validTotalCost() {
		Period period = new Period(LocalDateTime.MIN, LocalDateTime.MAX);
		String title = "테스트";
		String country = "대한민국";
		Long totalCost = -1L;
		String thumbnail = "";

		Assertions.assertThatThrownBy(() -> new Travelogue(period, title, country, totalCost, thumbnail))
			.isInstanceOf(IllegalArgumentException.class);
	}

}