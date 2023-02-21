package shop.zip.travel.global.error.exception;

import shop.zip.travel.global.error.ErrorCode;

public class CustomNotFoundException extends RuntimeException {

	private final ErrorCode errorCode;

	public CustomNotFoundException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}
