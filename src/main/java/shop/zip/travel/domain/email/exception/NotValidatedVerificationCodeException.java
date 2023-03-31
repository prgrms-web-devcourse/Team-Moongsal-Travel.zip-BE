package shop.zip.travel.domain.email.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.NotVerifiedCodeException;

public class NotValidatedVerificationCodeException extends NotVerifiedCodeException {

  public NotValidatedVerificationCodeException(ErrorCode errorCode) {
    super(errorCode);
  }
}
