package shop.zip.travel.global.error.exception;

import shop.zip.travel.global.error.BusinessException;
import shop.zip.travel.global.error.ErrorCode;

public class CustomNotFoundException extends BusinessException {

	public CustomNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

}
