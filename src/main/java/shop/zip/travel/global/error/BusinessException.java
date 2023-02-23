package shop.zip.travel.global.error;

public class BusinessException extends RuntimeException{

  private final ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

}
