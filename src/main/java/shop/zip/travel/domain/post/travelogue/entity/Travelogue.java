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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.util.Assert;
import shop.zip.travel.domain.base.BaseTimeEntity;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.data.Cost;
import shop.zip.travel.domain.post.travelogue.data.Period;

@Entity
public class Travelogue extends BaseTimeEntity {

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

	@Column(nullable = false)
	private String thumbnail;

	@Embedded
	@Column(nullable = false)
	private Cost cost;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "travelogue_id")
	private List<SubTravelogue> subTravelogues = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	protected Travelogue() {
	}

	public Travelogue(Period period, String title, Country country, String thumbnail, Cost cost,
		Member member) {
		this(period, title, country, thumbnail, cost, Collections.emptyList(), member);
	}

	public Travelogue(Period period, String title, Country country, String thumbnail, Cost cost,
		List<SubTravelogue> subTravelogues, Member member) {
		nullCheck(period, title, country, thumbnail, cost, subTravelogues, member);
		this.period = period;
		this.title = title;
		this.country = country;
		this.thumbnail = thumbnail;
		this.cost = cost;
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

	public List<SubTravelogue> getSubTravelogues() {
		return new ArrayList<>(subTravelogues);
	}

	public Member getMember() {
		return member;
	}

	private void nullCheck(Period period, String title, Country country, String thumbnail,
		Cost cost,
		List<SubTravelogue> subTravelogues, Member member) {
		Assert.notNull(period, "날짜를 확인해주세요");
		Assert.notNull(title, "제목을 확인해주세요");
		Assert.notNull(country, "나라를 확인해주세요");
		Assert.notNull(thumbnail, "썸네일 url 을 확인해주세요");
		Assert.notNull(cost, "경비를 확인해주세요");
		Assert.notNull(subTravelogues, "썸네일 url 을 확인해주세요");
		Assert.notNull(member, "사용자를 확인해주세요");
	}

	public void add(SubTravelogue subTravelogue) {
		this.subTravelogues.add(subTravelogue);
	}

}