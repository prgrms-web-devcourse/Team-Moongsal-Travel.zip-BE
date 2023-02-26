package shop.zip.travel.domain.member.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.NotVerifiedCodeException;

public class NotVerifiedAuthorizationCodeException extends NotVerifiedCodeException {

  public NotVerifiedAuthorizationCodeException(ErrorCode errorCode) {
    super(errorCode);
  }
}
