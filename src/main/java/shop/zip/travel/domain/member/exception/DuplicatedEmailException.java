package shop.zip.travel.domain.member.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.DuplicatedException;

public class DuplicatedEmailException extends DuplicatedException {

  public DuplicatedEmailException(ErrorCode errorCode) {
    super(errorCode);
  }

}
