package shop.zip.travel.domain.post.image.dto;

import jakarta.validation.constraints.NotBlank;
import shop.zip.travel.domain.post.image.entity.TravelPhoto;

public record TravelPhotoCreateReq(
    @NotBlank
    String url
) {

    public TravelPhoto toEntity() {
        return new TravelPhoto(url);
    }

    public static TravelPhotoCreateReq toDto(TravelPhoto travelPhoto) {
        return new TravelPhotoCreateReq(
            travelPhoto.getUrl()
        );
    }
}
