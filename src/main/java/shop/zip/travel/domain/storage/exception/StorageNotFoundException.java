package shop.zip.travel.domain.storage.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.CustomNotFoundException;

public class StorageNotFoundException extends CustomNotFoundException {

  public StorageNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

}
