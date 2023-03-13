package shop.zip.travel.domain.bookmark.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import shop.zip.travel.domain.base.BaseTimeEntity;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;

@Entity
public class Bookmark extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "travelogue_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Travelogue travelogue;

  @ManyToOne
  @JoinColumn(name = "member_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Member member;

  protected Bookmark() {
  }

  public Bookmark(Travelogue travelogue, Member member) {
    this.travelogue = travelogue;
    this.member = member;
  }

  public Long getId() {
    return id;
  }

}
