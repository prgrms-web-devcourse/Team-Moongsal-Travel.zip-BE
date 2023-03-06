package shop.zip.travel.domain.scrap.dto.res;

import java.util.List;
import java.util.stream.Collectors;
import shop.zip.travel.domain.scrap.document.Place;
import shop.zip.travel.domain.scrap.document.Scrap;

public record ScrapDetailRes(String title, List<Place> contents) {

  public static ScrapDetailRes toDto(Scrap scrap) {
    return new ScrapDetailRes(scrap.getTitle(), scrap.getContents());
  }
}
