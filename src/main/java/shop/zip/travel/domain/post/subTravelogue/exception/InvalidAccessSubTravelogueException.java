package shop.zip.travel.domain.post.subTravelogue.exception;

import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class InvalidAccessSubTravelogueException extends BusinessException {

  public InvalidAccessSubTravelogueException(ErrorCode errorCode) {
    super(errorCode);
  }
}
