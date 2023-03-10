package shop.zip.travel.global.error.exception;

import java.net.BindException;
import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class EmailException extends BusinessException {

  protected EmailException(ErrorCode errorCode) {
    super(errorCode);
  }
}
