package shop.zip.travel.domain.member.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.InvalidTokenException;

public class InvalidRefreshTokenException extends InvalidTokenException {

  public InvalidRefreshTokenException(ErrorCode errorCode) {
    super(errorCode);
  }
}
