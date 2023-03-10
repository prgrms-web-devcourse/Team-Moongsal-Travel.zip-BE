package shop.zip.travel.domain.suggestion.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import shop.zip.travel.domain.base.BaseTimeEntity;
import shop.zip.travel.domain.member.entity.Member;
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
}
