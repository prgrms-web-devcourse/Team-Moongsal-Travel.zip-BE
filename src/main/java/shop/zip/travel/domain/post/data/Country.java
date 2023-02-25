package shop.zip.travel.domain.post.data;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.util.Assert;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Country {

	private static final String KOREAN = "ko";
	private static final List<String> LOCALES = Arrays.stream(Locale.getISOCountries())
		.map(country -> new Locale(KOREAN, country).getDisplayCountry())
		.toList();

	@Column(name = "country_name", nullable = false)
	private String name;

	public Country(String name) {
		verify(name);
		this.name = name;
	}

	protected Country() {
	}

	private void verify(String inputCountryName) {
		Assert.notNull(inputCountryName, "국가명을 확인해주세요");
		verifyCountryName(inputCountryName);
	}

	private void verifyCountryName(String inputCountryName) {
		Assert.isTrue(LOCALES.contains(inputCountryName), "국가명을 확인해주세요");
	}

	public String getName() {
		return name;
	}
}
