package shop.zip.travel.domain.suggestion.entity;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

@RedisHash(timeToLive = 60 * 60 * 24 * 7)
public class Suggestion {

  @Id
  private String id;

  private Travelogue travelogue;

  @Indexed
  private Long memberId;

  protected Suggestion() {
  }

  public Suggestion(Travelogue travelogue, Long memberId) {
    this.travelogue = travelogue;
    this.memberId = memberId;
  }

  public Travelogue getTravelogue() {
    return travelogue;
  }

  public Long getMemberId() {
    return memberId;
  }

  public String getCountryName() {
    return this.travelogue.getCountry().getName();
  }
}
