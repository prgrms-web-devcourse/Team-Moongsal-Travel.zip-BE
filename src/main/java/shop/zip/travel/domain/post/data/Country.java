package shop.zip.travel.domain.post.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Country {

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
