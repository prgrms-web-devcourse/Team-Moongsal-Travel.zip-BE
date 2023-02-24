package shop.zip.travel.global.error.exception;

import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class NotVerifiedCodeException extends BusinessException {

  public NotVerifiedCodeException(ErrorCode errorCode) {
    super(errorCode);
  }
}
