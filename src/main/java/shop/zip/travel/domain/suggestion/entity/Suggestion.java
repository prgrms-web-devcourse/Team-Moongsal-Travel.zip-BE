package shop.zip.travel.domain.suggestion.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import shop.zip.travel.domain.base.BaseTimeEntity;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

@Entity
public class Suggestion extends BaseTimeEntity {

  @Id
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Travelogue travelogue;

  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  protected Suggestion() {
  }

  public Suggestion(Travelogue travelogue, Member member) {
    this.travelogue = travelogue;
    this.member = member;
  }

  public Travelogue getTravelogue() {
    return travelogue;
  }

  public Member getMember() {
    return member;
  }
}
