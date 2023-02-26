package shop.zip.travel.domain.post.subTravelogue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import shop.zip.travel.domain.post.image.dto.TravelPhotoCreateReq;
import shop.zip.travel.domain.post.subTravelogue.data.Address;
import shop.zip.travel.domain.post.subTravelogue.data.Transportation;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;

public record SubTravelogueCreateReq(
    @NotBlank
    String title,
    @NotBlank
    String content,
    @NotNull @Size(min = 1)
    List<Address> addresses,
    @NotNull @Size(min = 1)
    Set<Transportation> transportationSet,
    @NotNull @Size(min = 1)
    List<TravelPhotoCreateReq> travelPhotoCreateReqs
) {

    public SubTravelogue toEntity() {
        return new SubTravelogue(
            this.title,
            this.content,
            this.addresses,
            this.transportationSet,
            this.travelPhotoCreateReqs.stream()
                .map(TravelPhotoCreateReq::toEntity)
                .toList()
        );
    }
}
