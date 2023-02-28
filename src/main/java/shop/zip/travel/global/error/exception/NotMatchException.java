package shop.zip.travel.global.error.exception;

import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class NotMatchException extends BusinessException {

  public NotMatchException(ErrorCode errorCode) {
    super(errorCode);
  }
}
