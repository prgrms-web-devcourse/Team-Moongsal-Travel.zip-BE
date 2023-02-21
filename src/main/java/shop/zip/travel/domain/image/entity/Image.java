package shop.zip.travel.domain.image.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import shop.zip.travel.domain.post.travelog.entity.SubTravelogue;
import shop.zip.travel.domain.base.BaseImage;

@Entity
public class Image implements BaseImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_travelogue_id")
	private SubTravelogue subTravelogue;

	protected Image() {
	}

	public Image(String url, SubTravelogue subTravelogue) {
		this.url = url;
		this.subTravelogue = subTravelogue;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public SubTravelogue getSubTravelogue() {
		return subTravelogue;
	}

}
