package shop.zip.travel.domain.email.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.EmailException;

public class SendEmailException extends EmailException {

  public SendEmailException(ErrorCode errorCode) {
    super(errorCode);
  }
}
