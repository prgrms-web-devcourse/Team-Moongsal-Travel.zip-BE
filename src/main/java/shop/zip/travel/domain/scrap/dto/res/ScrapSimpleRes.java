package shop.zip.travel.domain.scrap.dto.res;

import shop.zip.travel.domain.scrap.document.Scrap;

public record ScrapSimpleRes(String storageObjectId, String title) {

  public static ScrapSimpleRes toDto(Scrap scrap) {
    return new ScrapSimpleRes(scrap.getId().toString(), scrap.getTitle());
  }
}
