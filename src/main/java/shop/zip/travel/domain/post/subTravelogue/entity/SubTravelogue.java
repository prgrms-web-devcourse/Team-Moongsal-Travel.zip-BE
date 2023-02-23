package shop.zip.travel.domain.post.subTravelogue.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import shop.zip.travel.domain.base.BaseTimeEntity;
import shop.zip.travel.domain.post.image.entity.TravelPhoto;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;

@Entity
public class SubTravelogue extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String title;

	@Column(columnDefinition = "LONGTEXT", nullable = false)
	private String content;

	@ElementCollection
	@CollectionTable(name = "address", joinColumns = @JoinColumn(name = "sub_travelogue_id"))
	private List<Address> addresses;

	@ElementCollection
	@CollectionTable(name = "transportation", joinColumns = @JoinColumn(name = "sub_travelogue_id"))
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Set<Transportation> transportationSet;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "sub_travelogue_id")
	private List<TravelPhoto> photos = new ArrayList<>();

	protected SubTravelogue() {
	}

	public SubTravelogue(String title, String content, List<Address> addresses, Set<Transportation> transportationSet,
		List<TravelPhoto> photos) {
		this.title = title;
		this.content = content;
		this.addresses = addresses;
		this.transportationSet = transportationSet;
		this.photos = photos;
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

	public List<Address> getAddresses() {
		return addresses;
	}

	public Set<Transportation> getTransportationSet() {
		return transportationSet;
	}

	public List<TravelPhoto> getPhotos() {
		return new ArrayList<>(photos);
	}
}
