package shop.zip.travel.domain.storage.dto.res;

import shop.zip.travel.domain.storage.document.Storage;

public record ScrapSimpleRes(String storageObjectId, String title) {

  public static ScrapSimpleRes toDto(Storage storage) {
    return new ScrapSimpleRes(storage.getId().toString(), storage.getTitle());
  }
}
