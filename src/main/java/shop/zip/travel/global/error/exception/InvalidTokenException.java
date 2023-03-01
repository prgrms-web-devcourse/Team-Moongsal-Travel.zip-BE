package shop.zip.travel.global.error.exception;

import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class InvalidTokenException extends BusinessException {

  public InvalidTokenException(ErrorCode errorCode) {
    super(errorCode);
  }
}
