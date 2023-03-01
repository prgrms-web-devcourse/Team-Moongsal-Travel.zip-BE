package shop.zip.travel.domain.member.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.InvalidTokenException;

public class InvalidAccessTokenException extends InvalidTokenException {

  public InvalidAccessTokenException(ErrorCode errorCode) {
    super(errorCode);
  }
}
