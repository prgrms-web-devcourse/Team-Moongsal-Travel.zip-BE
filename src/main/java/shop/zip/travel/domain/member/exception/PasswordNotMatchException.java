package shop.zip.travel.domain.member.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.NotMatchException;

public class PasswordNotMatchException extends NotMatchException {

  public PasswordNotMatchException(ErrorCode errorCode) {
    super(errorCode);
  }
}
