package shop.zip.travel.domain.member.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.DuplicatedException;

public class DuplicatedNicknameException extends DuplicatedException {

  public DuplicatedNicknameException(ErrorCode errorCode) {
    super(errorCode);
  }
}
