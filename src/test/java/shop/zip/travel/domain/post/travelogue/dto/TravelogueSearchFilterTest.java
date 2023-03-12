package shop.zip.travel.domain.post.travelogue.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TravelogueSearchFilterTest {

  @Test
  @DisplayName("시작날짜가 종료날짜보다 후 일 경우 IllegalArgumentException 을 던진다")
  void fail_search_by_period() {
    //given
    Long minDays = 2L;
    Long maxDays = 0L;

    assertThatThrownBy(() -> new TravelogueSearchFilter(minDays, maxDays, null, null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("최소금액이 최대금액보다 클 경우 IllegalArgumentException 을 던진다")
  void fail_search_by_cost() {
    //given
    Long minCost = 1000000L;
    Long maxCost = 500000L;

    assertThatThrownBy(() -> new TravelogueSearchFilter(null, null, minCost, maxCost))
        .isInstanceOf(IllegalArgumentException.class);
  }

}