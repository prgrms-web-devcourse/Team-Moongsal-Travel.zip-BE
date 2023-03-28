package shop.zip.travel.domain.post.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Country {

	@Column(name = "country_name")
	private String name;

	public Country(String name) {
		this.name = name;
	}

	protected Country() {
	}

	public String getName() {
		return name;
	}

	public boolean cannotPublish() {
		return Objects.isNull(name) || name.isBlank();
	}
}
