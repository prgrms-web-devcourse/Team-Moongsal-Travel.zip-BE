package shop.zip.travel.domain.suggestion.entity;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(timeToLive = 60 * 60 * 24 * 7)
public class Suggestion {

  @Id
  private String id;

  private String countryName;

  @Indexed
  private Long memberId;

  protected Suggestion() {
  }

  public Suggestion(String country, Long memberId) {
    this.countryName = country;
    this.memberId = memberId;
  }

  public String getCountryName() {
    return countryName;
  }

  public Long getMemberId() {
    return memberId;
  }
}
