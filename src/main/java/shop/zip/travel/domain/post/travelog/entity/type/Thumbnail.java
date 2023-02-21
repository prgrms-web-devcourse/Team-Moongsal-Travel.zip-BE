package shop.zip.travel.domain.post.travelog.entity.type;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import shop.zip.travel.domain.base.BaseImage;

@Embeddable
public class Thumbnail implements BaseImage {

	@Column(nullable = false)
	private String url;

	protected Thumbnail() {
	}

	public Thumbnail(String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return this.url;
	}
}
