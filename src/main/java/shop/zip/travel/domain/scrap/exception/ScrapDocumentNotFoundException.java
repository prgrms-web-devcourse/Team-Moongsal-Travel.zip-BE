package shop.zip.travel.domain.scrap.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.CustomNotFoundException;

public class ScrapDocumentNotFoundException extends CustomNotFoundException {

  public ScrapDocumentNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

}
