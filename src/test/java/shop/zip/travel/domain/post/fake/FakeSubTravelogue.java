package shop.zip.travel.domain.post.fake;

import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;

public class FakeSubTravelogue extends SubTravelogue {

  private final Long id;

  public FakeSubTravelogue(Long id, SubTravelogue subTravelogue) {
    super(
        subTravelogue.getTitle(),
        subTravelogue.getContent(),
        subTravelogue.getDay(),
        subTravelogue.getAddresses(),
        subTravelogue.getTransportationSet(),
        subTravelogue.getPhotos()
    );
    this.id = id;
  }

  @Override
  public Long getId() {
    return id;
  }
}
