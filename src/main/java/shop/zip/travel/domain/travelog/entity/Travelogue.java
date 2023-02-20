package shop.zip.travel.domain.travelog.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import shop.zip.travel.global.entity.BaseTimeEntity;

@Entity
public class Travelogue extends BaseTimeEntity {

	private static final String KOREAN = "ko";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Period period;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String country;

	@Embedded
	@Column(nullable = false)
	private Cost cost;

	@Column(nullable = false)
	private String thumbnail;

	protected Travelogue() {
	}

	public Travelogue(Period period, String title, String country, Cost cost, String thumbnail) {
		verifyCountry(country);
		this.period = period;
		this.title = title;
		this.country = country;
		this.cost = cost;
		this.thumbnail = thumbnail;
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

	public String getCountry() {
		return country;
	}

	public Cost getCost() {
		return cost;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	private void verifyCountry(String inputCountryName) {
		List<String> countries = Arrays.stream(Locale.getISOCountries())
			.map(country -> new Locale(KOREAN, country).getDisplayCountry())
			.toList();
		Assert.isTrue(countries.contains(inputCountryName), "국가명을 확인해주세요");
	}

}
