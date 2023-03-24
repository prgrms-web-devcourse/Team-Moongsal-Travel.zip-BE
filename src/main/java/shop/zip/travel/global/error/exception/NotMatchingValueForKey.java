package shop.zip.travel.global.error.exception;

import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class NotMatchingValueForKey extends BusinessException {

  public NotMatchingValueForKey(ErrorCode errorCode) {
    super(errorCode);
  }
}
