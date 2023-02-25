package shop.zip.travel.domain.member.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.CustomNotFoundException;

public class MemberNotFoundException extends CustomNotFoundException {

	public MemberNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
