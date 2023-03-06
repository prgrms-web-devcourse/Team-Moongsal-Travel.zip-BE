package shop.zip.travel.domain.scrap.document;


import jakarta.persistence.Id;
import org.bson.types.ObjectId;

public class Place {

  @Id
  String objectId;

  String placeName;

  public Place(String placeName) {
    this.objectId = ObjectId.get().toString();
    this.placeName = placeName;
  }

  protected Place() {

  }

  public String getObjectId() {
    return objectId;
  }

  public String getPlaceName() {
    return placeName;
  }

}
