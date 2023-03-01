package shop.zip.travel.domain.post.subTravelogue.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import shop.zip.travel.domain.post.data.DefaultValue;

@Embeddable
public class Address {

    @Column(nullable = false)
    private String spot;

    public Address() {
    }

    public Address(String spot) {
        this.spot = spot;
    }

    public String getSpot() {
        return spot;
    }

    public boolean cannotPublish() {
        return DefaultValue.STRING.isEqual(spot);
    }
}

