package shop.zip.travel.domain.post.subTravelogue.exception;

import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class SubTravelogueNotFoundException extends BusinessException {

  public SubTravelogueNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
