package shop.zip.travel.domain.post.subTravelogue.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import shop.zip.travel.domain.post.data.DefaultValue;

@Embeddable
public class Address {

    @Column(nullable = false)
    private String region;

    public Address() {
    }

    public Address(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public boolean cannotPublish() {
        return DefaultValue.STRING.isEqual(region);
    }
}

