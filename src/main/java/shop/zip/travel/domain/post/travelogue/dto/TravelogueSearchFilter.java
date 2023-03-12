package shop.zip.travel.domain.post.travelogue.dto;

import io.jsonwebtoken.lang.Assert;
import java.util.Objects;

public record TravelogueSearchFilter(
    Long minDays,
    Long maxDays,
    Long minCost,
    Long maxCost
) {

  public static final String DAYS_ERROR_MSG = "최대일수는 최소일수보다 작을 수 없습니다";
  public static final String COSTS_ERROR_MSG = "최대금액은 최소금액보다 작을 수 없습니다";

  public TravelogueSearchFilter(Long minDays, Long maxDays, Long minCost, Long maxCost) {
    verifyValues(minDays, maxDays, minCost, maxCost);
    this.minDays = minDays;
    this.maxDays = maxDays;
    this.minCost = minCost;
    this.maxCost = maxCost;
  }

  private void verifyValues(Long minDays, Long maxDays, Long minCost, Long maxCost) {
    verifyValuesEntered(minCost, maxCost, COSTS_ERROR_MSG);
    verifyValuesEntered(minDays, maxDays, DAYS_ERROR_MSG);
  }

  private void verifyValuesEntered(Long min, Long max, String message) {
    if (Objects.nonNull(min) || Objects.nonNull(max)) {
      Assert.notNull(min, message);
      Assert.notNull(max, message);

      verifyMaxGreaterThanMin(min, max, message);
    }
  }

  private void verifyMaxGreaterThanMin(Long min, Long max, String message) {
    if (max < min) {
      throw new IllegalArgumentException(message);
    }
  }

}

