package shop.zip.travel.domain.post.travelogue.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CostTest {

	@Test
	@DisplayName("총 가격 하나만으로 가격 객체를 생성할 수 있다.")
	void cost_create_success_by_total(){
		Long actualTotalCost = 30000000000L;
		Cost cost = new Cost(actualTotalCost);

		// when
		Long expectedTotalCost = cost.getTotal();

		// then
		assertThat(actualTotalCost).isEqualTo(expectedTotalCost);
	}

	@Test
	@DisplayName("세부 가격으로 가격 객체를 생성할 수 있다.")
	void cost_create_success_by_detail(){
		Long actualTransportationCost = 400000L;
		Long actualLodgeCost = 500000L;
		Long actualEtcCost = 100000L;
		Long actualTotalCost = actualTransportationCost + actualLodgeCost + actualEtcCost;
		Cost cost = new Cost(actualTransportationCost, actualLodgeCost, actualEtcCost, actualTotalCost);

		// when
		Long expectedTotalCost =  cost.getTotal();
		Long expectedLodgeCost = cost.getLodge();
		Long expectedTransportationCost = cost.getTransportation();
		Long expectedEtcCost = cost.getEtc();

		// then
		assertThat(actualEtcCost).isEqualTo(expectedEtcCost);
		assertThat(actualTotalCost).isEqualTo(expectedTotalCost);
		assertThat(actualLodgeCost).isEqualTo(expectedLodgeCost);
		assertThat(actualTransportationCost).isEqualTo(expectedTransportationCost);
	}

	@Test
	@DisplayName("가격이 0원보다 작을 수 없다.")
	void cost_should_not_be_smaller_than_zero(){
		assertThatThrownBy(() -> new Cost(-100L))
			.isInstanceOf(IllegalArgumentException.class);
	}
}