package shop.zip.travel.domain.post.image.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.util.Assert;

@Entity
public class TravelPhoto {

    private static final int MIN_LENGTH = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    protected TravelPhoto() {
    }

    public TravelPhoto(String url) {
        verify(url);
        this.url = url;
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
