package shop.zip.travel.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다"),
	DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이메일이 중복입니다"),
	DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "닉네임이 중복입니다"),
	NOT_VERIFIED_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다"),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다"),
	EMAIL_NOT_MATCH(HttpStatus.BAD_REQUEST, "이메일이 일치하지 않습니다"),
	PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "패스워드가 일치하지 않습니다"),
	TRAVELOGUE_NOT_FOUND(HttpStatus.NOT_FOUND, "여행기를 찾을 수 없습니다"),
	NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "로그인후 이용이 가능합니다");


	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return this.status;
	}

	public String getMessage() {
		return this.message;
	}

	public int getStatusValue() {
		return this.status.value();
	}

}
