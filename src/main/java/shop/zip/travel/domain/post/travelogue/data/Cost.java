package shop.zip.travel.domain.post.travelogue.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public class Cost {

    private static final int ZERO = 0;

    @Column(nullable = true)
    private Long transportation;

    @Column(nullable = true)
    private Long lodge;

    @Column(nullable = true)
    private Long etc;

    @Column(nullable = false)
    private Long total;

    protected Cost() {
    }

    public Cost(Long transportation, Long lodge, Long etc, Long total) {
        verify(total);
        this.transportation = transportation;
        this.lodge = lodge;
        this.etc = etc;
        this.total = total;
    }

    public Cost(Long total) {
        this(null, null, null, total);
    }

    private void verify(Long total) {
        Assert.notNull(total, "총 금액을 확인해주세요");
        Assert.isTrue(total >= ZERO, "총 금액을 확인해주세요");
    }

    public Long getTransportation() {
        return transportation;
    }

    public Long getLodge() {
        return lodge;
    }

    public Long getEtc() {
        return etc;
    }

    public Long getTotal() {
        return total;
    }

}
