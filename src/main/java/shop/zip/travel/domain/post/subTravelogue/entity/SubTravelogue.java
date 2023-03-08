package shop.zip.travel.domain.post.subTravelogue.entity;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.util.Assert;
import shop.zip.travel.domain.base.BaseTimeEntity;
import shop.zip.travel.domain.post.image.entity.TravelPhoto;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.travelogue.exception.InvalidPublishTravelogueException;
import shop.zip.travel.global.error.ErrorCode;

@Entity
public class SubTravelogue extends BaseTimeEntity {

    private static final int ZERO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @ElementCollection
    @CollectionTable(name = "address", joinColumns = @JoinColumn(name = "sub_travelogue_id"))
    private List<Address> addresses = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "transportation", joinColumns = @JoinColumn(name = "sub_travelogue_id"))
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Set<Transportation> transportationSet = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sub_travelogue_id")
    private List<TravelPhoto> photos = new ArrayList<>();

    protected SubTravelogue() {
    }

    public SubTravelogue(String title, String content, List<Address> addresses,
        Set<Transportation> transportationSet, List<TravelPhoto> photos) {
        verify(title, content, addresses, transportationSet, photos);
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
        return new ArrayList<>(addresses);
    }

    public Set<Transportation> getTransportationSet() {
        return new HashSet<>(transportationSet);
    }

    public List<TravelPhoto> getPhotos() {
        return new ArrayList<>(photos);
    }

    private void verify(String title, String content, List<Address> addresses,
        Set<Transportation> transportationSet, List<TravelPhoto> photos) {
        nullCheck(title, content, addresses, transportationSet, photos);
    }


    private void nullCheck(String title, String content, List<Address> addresses,
        Set<Transportation> transportationSet, List<TravelPhoto> photos) {
        Assert.notNull(title, "제목을 확인해주세요");
        Assert.notNull(content, "내용을 확인해주세요");
        Assert.notNull(addresses, "주소를 확인해주세요");
        Assert.notNull(transportationSet, "이동수단을 확인해주세요");
        Assert.notNull(photos, "이미지를 확인해주세요");
    }

    private boolean cannotPublish() {
        return title.isBlank() ||
            content.isBlank() ||
            addresses.size() == ZERO;
    }

    public void verifyPublish() {
        if (cannotPublish()) {
            throw new InvalidPublishTravelogueException(ErrorCode.CANNOT_PUBLISH_TRAVELOGUE);
        }
        this.addresses.forEach(Address::verifyPublish);
    }
}
