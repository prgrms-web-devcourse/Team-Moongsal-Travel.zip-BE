package shop.zip.travel.domain.post.travelogue.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.springframework.util.Assert;
import shop.zip.travel.domain.base.BaseTimeEntity;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.subTravelogue.exception.InvalidAccessSubTravelogueException;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;
import shop.zip.travel.domain.post.travelogue.dto.req.TravelogueUpdateReq;
import shop.zip.travel.domain.post.travelogue.exception.InvalidPublishTravelogueException;
import shop.zip.travel.domain.post.travelogue.exception.NoAuthorizationException;
import shop.zip.travel.global.error.ErrorCode;

@Entity
public class Travelogue extends BaseTimeEntity {

  private static final boolean TEMP = false;
  private static final int INDEX_MATCHER = 1;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  @Column(nullable = false)
  private Period period;

  @Column
  private String title;

  @Embedded
  @Column(nullable = false)
  private Country country;

  @Lob
  private String thumbnail;

  @Embedded
  @Column(nullable = false)
  private Cost cost;

  @Column(nullable = false)
  private boolean isPublished;

  @Column(nullable = false)
  private Long viewCount;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "travelogue_id")
  private List<SubTravelogue> subTravelogues = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  protected Travelogue() {
  }

  public Travelogue(Period period, String title, Country country, String thumbnail, Cost cost,
      boolean isPublished, Member member) {
    this(period, title, country, thumbnail, cost, isPublished, 0L, new ArrayList<>(), member);
  }

  public Travelogue(Period period, String title, Country country, String thumbnail, Cost cost,
      boolean isPublished, List<SubTravelogue> subTravelogues, Member member) {
    this(period, title, country, thumbnail, cost, isPublished, 0L, subTravelogues, member);
  }

  public Travelogue(Period period, String title, Country country, String thumbnail, Cost cost,
      boolean isPublished, Long viewCount, List<SubTravelogue> subTravelogues, Member member) {
    checkNull(member);
    this.period = period;
    this.title = title;
    this.country = country;
    this.thumbnail = thumbnail;
    this.cost = cost;
    this.isPublished = isPublished;
    this.viewCount = viewCount;
    this.subTravelogues = subTravelogues;
    this.member = member;
  }

  public Long getId() {
    return id;
  }

  public Period getPeriod() {
    return period;
  }

  public String getTitle() {
    return title;
  }

  public Country getCountry() {
    return country;
  }

  public Cost getCost() {
    return cost;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public boolean getIsPublished() {
    return isPublished;
  }

  public List<SubTravelogue> getSubTravelogues() {
    return new ArrayList<>(subTravelogues);
  }

  public Member getMember() {
    return member;
  }

  public Long getViewCount() {
    return viewCount;
  }


  public void addSubTravelogue(SubTravelogue subTravelogue) {
    verifySubTravelogueDuplicate(subTravelogue);

    if (hasAllSubTravelogues()) {
      throw new IllegalArgumentException("모든 서브트래블로그가 작성되어 있습니다.");
    }

    this.subTravelogues.add(subTravelogue);
  }

  private void verifySubTravelogueDuplicate(SubTravelogue subTravelogue) {
    Assert.isTrue(!subTravelogues.contains(subTravelogue), "이미 존재하는 서브게시물 입니다.");
  }

  public void update(TravelogueUpdateReq travelogueUpdateReq) {
    this.period = travelogueUpdateReq.getPeriod();
    this.title = travelogueUpdateReq.getTitle();
    this.country = travelogueUpdateReq.getCountry();
    this.cost = travelogueUpdateReq.getCost();
    this.thumbnail = travelogueUpdateReq.getThumbnail();
    this.isPublished = TEMP;
  }

  public void changePublishStatus() {
    if (cannotPublish()) {
      throw new InvalidPublishTravelogueException(ErrorCode.CANNOT_PUBLISH_TRAVELOGUE);
    }
    this.subTravelogues.forEach(SubTravelogue::verifyPublish);
    this.isPublished = true;
  }

  public void validWriter(Long memberId) {
    if (!this.member.getId().equals(memberId)) {
      throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION_TO_TRAVELOGUE);
    }
  }

  public void addViewCount() {
    this.viewCount++;
  }

  public void updateSubTravelogues(SubTravelogue newSubTravelogue) {
    removeOldSubTravelogue(newSubTravelogue.getDay() - INDEX_MATCHER);

    this.subTravelogues.add(newSubTravelogue);
    subTravelogues.sort(Comparator.comparingInt(SubTravelogue::getDay));
  }

  public void isContain(SubTravelogue subTravelogue) {
    if (!this.getSubTravelogues().contains(subTravelogue)) {
      throw new InvalidAccessSubTravelogueException(
          ErrorCode.TRAVELOGUE_NOT_CONTAIN_SUB_TRAVELOGUE
      );
    }
  }

  private void removeOldSubTravelogue(int idx) {
    this.subTravelogues.remove(idx);
  }

  private boolean hasAllSubTravelogues() {
    return subTravelogues.size() == period.getNights() + 1;
  }

  private boolean cannotPublish() {
    return hasNull() ||
        !hasAllSubTravelogues() ||
        period.cannotPublish() ||
        cost.cannotPublish() ||
        country.cannotPublish();
  }

  private boolean hasNull() {
    return Objects.isNull(title) ||
        Objects.isNull(thumbnail);
  }

  private void checkNull(Member member) {
    Assert.notNull(member, "사용자를 확인해주세요");
  }
}
