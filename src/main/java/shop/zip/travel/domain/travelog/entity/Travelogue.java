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
	private static final int ZERO = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Period period;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String country;

	@Column(nullable = false)
	private Long totalCost;

	@Column(nullable = false)
	private String thumbnail;

	protected Travelogue() {
	}

	public Travelogue(Period period, String title, String country, Long totalCost, String thumbnail) {
		valid(country, totalCost);
		this.period = period;
		this.title = title;
		this.country = country;
		this.totalCost = totalCost;
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

	public Long getTotalCost() {
		return totalCost;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	private void valid(String inputCountryName, Long totalCost) {
		validCost(totalCost);
		validCountry(inputCountryName);
	}

	private void validCountry(String inputCountryName) {
		List<String> countries = Arrays.stream(Locale.getISOCountries())
			.map(country -> new Locale(KOREAN, country).getDisplayCountry())
			.toList();
		Assert.isTrue(countries.contains(inputCountryName), "국가명을 확인해주세요");
	}

	private void validCost(Long totalCost) {
		Assert.isTrue(totalCost > ZERO, "총 금액을 확인해주세요");
	}

}
