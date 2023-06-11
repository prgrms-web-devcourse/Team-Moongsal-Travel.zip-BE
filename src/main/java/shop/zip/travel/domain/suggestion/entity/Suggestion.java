package shop.zip.travel.domain.suggestion.entity;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(timeToLive = 60 * 60 * 24 * 7)
public class Suggestion {

  @Id
  private String id;

  private Long travelogueId;

  private String countryName;

  private String title;

  private String thumbnail;

  private Long totalCost;

  @Indexed
  private Long memberId;

  protected Suggestion() {
  }

  public Suggestion(Long travelogueId, String countryName, String title, String thumbnail,
      Long totalCost, Long memberId) {
    this.travelogueId = travelogueId;
    this.countryName = countryName;
    this.title = title;
    this.thumbnail = thumbnail;
    this.totalCost = totalCost;
    this.memberId = memberId;
  }

  public String getId() {
    return id;
  }

  public Long getTravelogueId() {
    return travelogueId;
  }

  public String getCountryName() {
    return countryName;
  }

  public String getTitle() {
    return title;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public Long getTotalCost() {
    return totalCost;
  }

  public Long getMemberId() {
    return memberId;
  }
}
