package shop.zip.travel.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다"),
    TRAVELOGUE_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다");

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
