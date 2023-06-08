package shop.zip.travel.domain.post.travelogue.data;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

class TravelogueTest {

	private final String thumbnail = "www.naver.com";
	private final String title = "메인 게시물";
	private final boolean isPublished = true;

	@Test
	@DisplayName("작성자 없이 메인 게시물을 생성할 수 없다.")
	void create_fail_by_no_member(){
		assertThatThrownBy(() -> new Travelogue(
				DummyGenerator.createPeriod(),
				title,
				DummyGenerator.createCountry(),
				thumbnail,
				DummyGenerator.createCost(),
				isPublished,
				List.of(DummyGenerator.createSubTravelogue(1)),
				null
		)).isInstanceOf(IllegalArgumentException.class);
	}
}