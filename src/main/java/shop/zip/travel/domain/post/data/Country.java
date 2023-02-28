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
		this.name = name;
	}

	protected Country() {
	}

	public String getName() {
		return name;
	}
}
