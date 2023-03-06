package shop.zip.travel.domain.post.travelogue.exception;

import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class InvalidPublishTravelogueException extends BusinessException {

  public InvalidPublishTravelogueException(ErrorCode errorCode) {
    super(errorCode);
  }
}
