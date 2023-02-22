package shop.zip.travel.domain.post.travelog.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import shop.zip.travel.domain.base.BaseTimeEntity;
import shop.zip.travel.domain.post.subTravelog.entity.SubTravelogue;
import shop.zip.travel.domain.post.data.Country;
import shop.zip.travel.domain.post.travelog.data.Cost;
import shop.zip.travel.domain.post.travelog.data.Period;

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

	protected Travelogue() {
	}

	public Travelogue(Period period, String title, Country country, String thumbnail, Cost cost,
		List<SubTravelogue> subTravelogues) {
		this.period = period;
		this.title = title;
		this.country = country;
		this.thumbnail = thumbnail;
		this.cost = cost;
		this.subTravelogues = subTravelogues;
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

	public List<SubTravelogue> getSubTravelogue() {
		return new ArrayList<>(subTravelogues);
	}
}
