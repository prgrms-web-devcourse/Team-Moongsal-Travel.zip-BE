package shop.zip.travel.domain.post.travelogue.exception;

import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class NoAuthorizationException extends BusinessException {

  public NoAuthorizationException(ErrorCode errorCode) {
    super(errorCode);
  }
}
