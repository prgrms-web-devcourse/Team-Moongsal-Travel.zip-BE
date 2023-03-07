package shop.zip.travel.domain.post.subTravelogue.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import shop.zip.travel.domain.post.data.DefaultValue;
import shop.zip.travel.domain.post.travelogue.exception.InvalidPublishTravelogueException;
import shop.zip.travel.global.error.ErrorCode;

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

    public void verifyPublish() {
        if (cannotPublish()) {
            throw new InvalidPublishTravelogueException(ErrorCode.CANNOT_PUBLISH_TRAVELOGUE);
        }
    }
}

