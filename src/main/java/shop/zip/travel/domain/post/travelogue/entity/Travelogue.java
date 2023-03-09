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
import java.util.List;
import org.springframework.util.Assert;
import shop.zip.travel.domain.base.BaseTimeEntity;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.data.DefaultValue;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueUpdate;
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
	private Period period;

	@Column(nullable = false)
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
		nullCheck(period, title, country, thumbnail, cost, subTravelogues, member);
		valid(title, thumbnail);
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

	private void nullCheck(Period period, String title, Country country, String thumbnail,
			Cost cost, List<SubTravelogue> subTravelogues, Member member) {
		Assert.notNull(period, "날짜를 확인해주세요");
		Assert.notNull(title, "제목을 확인해주세요");
		Assert.notNull(country, "나라를 확인해주세요");
		Assert.notNull(thumbnail, "썸네일 url 을 확인해주세요");
		Assert.notNull(cost, "경비를 확인해주세요");
		Assert.notNull(subTravelogues, "서브 게시물을 확인해주세요");
		Assert.notNull(member, "사용자를 확인해주세요");
	}

	private void valid(String title, String thumbnail){
		validTitle(title);
		validThumbnail(thumbnail);
	}


	private void validTitle(String inputTitle){
		if(inputTitle.isBlank()){
			throw new IllegalArgumentException("제목은 비어있을 수 없습니다.");
		}
	}

	private void validThumbnail(String thumbnail){
		if (thumbnail.isBlank()) {
			throw new IllegalArgumentException("썸네일은 빈 값으로 들어올 수 없습니다. 정확한 url이 필요합니다.");
		}
	}

	public void addSubTravelogue(SubTravelogue subTravelogue) {
		verifySubTravelogueDuplicate(subTravelogue);
		verifySubTraveloguesSize();
		this.subTravelogues.add(subTravelogue);
	}

	private void verifySubTraveloguesSize() {
		if (this.subTravelogues.size() > this.period.getNights() + 1) {
			throw new IllegalArgumentException("이미 모든 서브 트레블로그가 작성되어 있습니다.");
		}
	}

	private void verifySubTravelogueDuplicate(SubTravelogue subTravelogue) {
		Assert.isTrue(!subTravelogues.contains(subTravelogue), "이미 존재하는 서브게시물 입니다.");
	}

	public void update(TravelogueUpdate travelogueUpdate) {
		this.period = travelogueUpdate.period();
		this.title = travelogueUpdate.title();
		this.country = travelogueUpdate.country();
		this.cost = travelogueUpdate.cost();
		this.thumbnail = travelogueUpdate.thumbnail();
		this.isPublished = TEMP;
	}

	public void changePublishStatus() {
		if (cannotPublish()) {
			throw new InvalidPublishTravelogueException(ErrorCode.CANNOT_PUBLISH_TRAVELOGUE);
		}
		this.subTravelogues.forEach(SubTravelogue::verifyPublish);
		this.isPublished = true;
	}

	private boolean cannotPublish() {
		return period.cannotPublish() ||
				DefaultValue.STRING.isEqual(title) ||
				DefaultValue.STRING.isEqual(thumbnail) ||
				country.cannotPublish() ||
				cost.cannotPublish() ||
				subTravelogues.size() < getPeriod().getNights() + 1;
	}

	public void isWriter(Long memberId) {
		if (!this.member.getId().equals(memberId)) {
			throw new NoAuthorizationException(ErrorCode.NO_AUTHORIZATION_TO_TRAVELOGUE);
		}
	}

	public void addViewCount() {
		this.viewCount++;
	}

	public void updateSubTravelogues(SubTravelogue newSubTravelogue) {
		removeOldSubTravelogue(newSubTravelogue.getDay() - INDEX_MATCHER);
		List<SubTravelogue> newSubTravelogues = new ArrayList<>(this.subTravelogues);
		newSubTravelogues.add(newSubTravelogue);
		changeSubTravelogues(newSubTravelogues);
	}

	private void removeOldSubTravelogue(int idx) {
		this.subTravelogues.remove(idx);
	}

	private void changeSubTravelogues(List<SubTravelogue> newSubTravelogues) {
		this.subTravelogues.clear();
		newSubTravelogues.sort((sub1, sub2) -> {
			int day1 = sub1.getDay();
			int day2 = sub2.getDay();

			if (day1 > day2) {
				return 1;
			} else {
				return -1;
			}
		});
		this.subTravelogues.addAll(newSubTravelogues);
	}

}