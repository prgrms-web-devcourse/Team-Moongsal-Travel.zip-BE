package shop.zip.travel.domain.post.subTravelogue.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import shop.zip.travel.domain.post.data.Country;

@Embeddable
public class Address {

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String spot;

    public Address() {
    }

    public Address(Country country, String city, String spot) {
        this.country = country.getName();
        this.city = city;
        this.spot = spot;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getSpot() {
        return spot;
    }
}

