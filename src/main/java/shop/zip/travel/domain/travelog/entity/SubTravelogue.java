package shop.zip.travel.domain.travelog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class SubTravelogue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String title;

	@Column(columnDefinition = "LONGTEXT", nullable = false)
	private String content;

	@Embedded
	private Address address;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Transportation transportation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "travelogue_id")
	private Travelogue travelogue;

	protected SubTravelogue() {
	}

	public SubTravelogue(String title, String content, Address address, Transportation transportation,
		Travelogue travelogue) {
		this.title = title;
		this.content = content;
		this.address = address;
		this.transportation = transportation;
		this.travelogue = travelogue;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public Address getAddress() {
		return address;
	}

	public Transportation getTransportation() {
		return transportation;
	}

	public Travelogue getTravelogue() {
		return travelogue;
	}
}
