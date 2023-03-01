package shop.zip.travel.global.error.exception;

import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class UnauthorizedException extends BusinessException {

  public UnauthorizedException(ErrorCode errorCode) {
    super(errorCode);
  }
}
