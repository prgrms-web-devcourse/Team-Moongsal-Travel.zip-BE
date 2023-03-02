package shop.zip.travel.global.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.zip.travel.global.error.exception.CustomNotFoundException;
import shop.zip.travel.global.error.exception.DuplicatedException;
import shop.zip.travel.global.error.exception.InvalidTokenException;
import shop.zip.travel.global.error.exception.NotMatchException;
import shop.zip.travel.global.error.exception.NotVerifiedCodeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
		log.info("BindException : ", e);
		int httpStatus = ErrorCode.BINDING_WRONG.getStatusValue();
		String message = ErrorCode.BINDING_WRONG.getMessage();
		return ResponseEntity.status(httpStatus).body(new ErrorResponse(message));
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException e) {
		log.info("InvalidTokenException : ", e);
		int httpStatus = e.getErrorCode().getStatusValue();
		String message = e.getErrorCode().getMessage();
		return ResponseEntity.status(httpStatus).body(new ErrorResponse(message));
	}

	@ExceptionHandler(CustomNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCustomNotFoundException(CustomNotFoundException e) {
		log.info("CustomNotFoundException : ", e);
		int httpStatus = e.getErrorCode().getStatusValue();
		String message = e.getErrorCode().getMessage();
		return ResponseEntity.status(httpStatus).body(new ErrorResponse(message));
	}

	@ExceptionHandler(DuplicatedException.class)
	public ResponseEntity<ErrorResponse> handleDuplicatedException(DuplicatedException e) {
		log.info("DuplicatedException : ", e);
		int httpStatus = e.getErrorCode().getStatusValue();
		String message = e.getErrorCode().getMessage();
		return ResponseEntity.status(httpStatus).body(new ErrorResponse(message));
	}

	@ExceptionHandler(NotVerifiedCodeException.class)
	public ResponseEntity<ErrorResponse> handleNotVerifiedCodeException(NotVerifiedCodeException e) {
		log.info("NotVerifiedCodeException : ", e);
		int httpStatus = e.getErrorCode().getStatusValue();
		String message = e.getErrorCode().getMessage();
		return ResponseEntity.status(httpStatus).body(new ErrorResponse(message));
	}

	@ExceptionHandler(NotMatchException.class)
	public ResponseEntity<ErrorResponse> handleNotMatchException(NotMatchException e) {
		log.info("NotMatchException : ", e);
		int httpStatus = e.getErrorCode().getStatusValue();
		String message = e.getErrorCode().getMessage();
		return ResponseEntity.status(httpStatus).body(new ErrorResponse(message));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
		log.error("RuntimeException : ", e);
		return ResponseEntity.internalServerError().body(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		log.error("Exception : ", e);
		return ResponseEntity.internalServerError().build();
	}
}
