package shop.zip.travel.global.error.exception;

import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class DuplicatedException extends BusinessException {

  public DuplicatedException(ErrorCode errorCode) {
    super(errorCode);
  }

}
