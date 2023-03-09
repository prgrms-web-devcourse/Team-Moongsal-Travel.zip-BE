package shop.zip.travel.domain.storage.document;


import jakarta.persistence.Id;
import org.bson.types.ObjectId;

public class Place {

  @Id
  String scrapObjectId;

  String placeName;

  Long postId;

  public Place(String placeName, Long postId) {
    this.scrapObjectId = ObjectId.get().toString();
    this.placeName = placeName;
    this.postId = postId;
  }

  protected Place() {

  }

  public String getScrapObjectId() {
    return scrapObjectId;
  }

  public String getPlaceName() {
    return placeName;
  }

  public Long getPostId() {
    return postId;
  }
}
