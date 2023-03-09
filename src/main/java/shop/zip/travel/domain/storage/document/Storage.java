package shop.zip.travel.domain.storage.document;

import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "my_scrap")
public class Storage {

  @Id
  private ObjectId id;

  private Long memberId;

  private String title;

  private List<Place> contents;

  protected Storage() {

  }

  public Storage(Long memberId, String title) {
    this.memberId = memberId;
    this.title = title;
    this.contents = new ArrayList<>();
  }

  public ObjectId getId() {
    return id;
  }

  public Long getMemberId() {
    return memberId;
  }

  public String getTitle() {
    return title;
  }

  public List<Place> getContents() {
    return contents;
  }
}
