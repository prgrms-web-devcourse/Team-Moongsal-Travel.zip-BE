package shop.zip.travel.domain.member.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.NotMatchException;

public class EmailNotMatchException extends NotMatchException {

  public EmailNotMatchException(ErrorCode errorCode) {
    super(errorCode);
  }
}
