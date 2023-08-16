package shop.zip.travel.domain.post.travelogue.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Views {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private long count;

  public Views(long count) {
    this.count = count;
  }

  protected Views() {
  }

  public Long getId() {
    return id;
  }

  public long getCount() {
    return count;
  }

}
