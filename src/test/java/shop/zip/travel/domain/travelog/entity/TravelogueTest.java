package shop.zip.travel.domain.travelog.entity;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TravelogueTest {

	@Test
	@DisplayName("존재하지 않는 나라명을 입력할 경우 IllegalArgumentException 을 던진다")
	void verifyCountry() {
		Period period = new Period(LocalDateTime.now().minusDays(1L), LocalDateTime.now());
		String title = "테스트";
		String country = "짱구네";
		Cost cost = new Cost(10000L);
		String thumbnail = "";

		Assertions.assertThatThrownBy(() -> new Travelogue(period, title, country, cost, thumbnail))
			.isInstanceOf(IllegalArgumentException.class);
	}

}