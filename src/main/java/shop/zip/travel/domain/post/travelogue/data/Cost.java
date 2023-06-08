package shop.zip.travel.domain.post.travelogue.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import org.springframework.util.Assert;

@Embeddable
public class Cost {

  private static final int ZERO = 0;

  @Column
  private Long transportation;

  @Column
  private Long lodge;

  @Column
  private Long etc;

  @Column
  private Long total;

  protected Cost() {
  }

  public Cost(Long transportation, Long lodge, Long etc, Long total) {
    if (!Objects.isNull(total)) {
      verify(total);
    }
    this.transportation = transportation;
    this.lodge = lodge;
    this.etc = etc;
    this.total = total;
  }

  public Cost(Long total) {
    this(0L, 0L, 0L, total);
  }

  private void verify(Long total) {
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

  public boolean cannotPublish() {
    return Objects.isNull(total) || total <= ZERO;
  }
}
