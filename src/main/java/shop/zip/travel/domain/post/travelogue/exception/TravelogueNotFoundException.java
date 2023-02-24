package shop.zip.travel.domain.post.travelogue.exception;

import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.CustomNotFoundException;

public class TravelogueNotFoundException extends CustomNotFoundException {

    public TravelogueNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
