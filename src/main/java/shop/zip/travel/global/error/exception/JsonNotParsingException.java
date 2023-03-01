package shop.zip.travel.global.error.exception;

import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class JsonNotParsingException extends BusinessException {

  public JsonNotParsingException(ErrorCode errorCode) {
    super(errorCode);
  }
}
