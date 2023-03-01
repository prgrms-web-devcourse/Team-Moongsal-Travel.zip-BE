package shop.zip.travel.domain.member.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.UnauthorizedException;

public class NotLoggedInException extends UnauthorizedException {

  public NotLoggedInException(ErrorCode errorCode) {
    super(errorCode);
  }
}
