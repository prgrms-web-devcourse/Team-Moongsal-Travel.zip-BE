package shop.zip.travel.domain.scrap.document;


import jakarta.persistence.Id;
import org.bson.types.ObjectId;

public class Place {

  @Id
  String scrapObjectId;

  String placeName;

  public Place(String placeName) {
    this.scrapObjectId = ObjectId.get().toString();
    this.placeName = placeName;
  }

  protected Place() {

  }

  public String getScrapObjectId() {
    return scrapObjectId;
  }

  public String getPlaceName() {
    return placeName;
  }

}
