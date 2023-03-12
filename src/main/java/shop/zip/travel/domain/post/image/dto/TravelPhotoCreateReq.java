package shop.zip.travel.domain.post.image.dto;

import static shop.zip.travel.domain.post.data.DefaultValue.orGetStringReturnValue;

import shop.zip.travel.domain.post.data.DefaultValue;
import shop.zip.travel.domain.post.image.entity.TravelPhoto;

public record TravelPhotoCreateReq(
    String url
) {

    public TravelPhoto toEntity() {
        return new TravelPhoto(
            (url.isBlank()) ? DefaultValue.STRING.getValue() : url
        );
    }

    public static TravelPhotoCreateReq toDto(TravelPhoto travelPhoto) {
        return new TravelPhotoCreateReq(
            orGetStringReturnValue(travelPhoto.getUrl())
        );
    }
}
