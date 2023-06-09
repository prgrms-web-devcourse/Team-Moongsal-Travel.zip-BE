package shop.zip.travel.domain.post.image.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.util.Assert;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;

@Entity
public class TravelPhoto {

    private static final int MIN_LENGTH = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_travelogue_id")
    private SubTravelogue subTravelogue;

    protected TravelPhoto() {
    }

    public TravelPhoto(String url) {
        verify(url);
        this.url = url;
    }

    public void updateSubTravelogue(SubTravelogue subTravelogue) {
        this.subTravelogue = subTravelogue;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void verify(String url) {
        Assert.notNull(url, "이미지 url 을 확인해주세요");
        Assert.isTrue(url.length() > MIN_LENGTH, "url 은 한 글자 이상이여야 합니다");
    }
}
