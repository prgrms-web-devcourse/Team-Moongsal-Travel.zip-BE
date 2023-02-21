package shop.zip.travel.domain.travelog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import shop.zip.travel.global.entity.BaseTimeEntity;
import shop.zip.travel.domain.member.entity.Member;

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

	@Embedded
	@Column(nullable = false)
	private Cost cost;

	@Embedded
	@Column(nullable = false)
	private Thumbnail thumbnail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	protected Travelogue() {
	}

	public Travelogue(Period period, String title, Country country, Cost cost, Thumbnail thumbnail, Member member) {
		this.period = period;
		this.title = title;
		this.country = country;
		this.cost = cost;
		this.thumbnail = thumbnail;
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

	public Thumbnail getThumbnail() {
		return thumbnail;
	}

	public Member getMember() {
		return member;
	}

}
