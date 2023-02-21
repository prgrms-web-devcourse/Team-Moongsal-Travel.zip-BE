package shop.zip.travel.domain.post.travelog.entity;

import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import shop.zip.travel.domain.post.travelog.entity.type.Transportation;
import shop.zip.travel.domain.post.travelog.entity.type.Address;
import shop.zip.travel.domain.base.BaseTimeEntity;

@Entity
public class SubTravelogue extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String title;

	@Column(columnDefinition = "LONGTEXT", nullable = false)
	private String content;

	@Embedded
	private Address address;

	@ElementCollection
	@CollectionTable(name = "transportation", joinColumns = @JoinColumn(name = "sub_travelogue_id"))
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Set<Transportation> transportations;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "travelogue_id")
	private Travelogue travelogue;

	protected SubTravelogue() {
	}

	public SubTravelogue(String title, String content, Address address, Set<Transportation> transportations,
		Travelogue travelogue) {
		this.title = title;
		this.content = content;
		this.address = address;
		this.transportations = transportations;
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

	public Set<Transportation> getTransportations() {
		return transportations;
	}

	public Travelogue getTravelogue() {
		return travelogue;
	}

}
