package shop.zip.travel.domain.storage.dto.res;

import java.util.List;
import shop.zip.travel.domain.storage.document.Place;
import shop.zip.travel.domain.storage.document.Storage;

public record ScrapDetailRes(String title, List<Place> contents) {

  public static ScrapDetailRes toDto(Storage storage) {
    return new ScrapDetailRes(storage.getTitle(), storage.getContents());
  }
}
